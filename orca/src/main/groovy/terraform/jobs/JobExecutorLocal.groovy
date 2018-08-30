package terraform.jobs

import groovy.util.logging.Slf4j
import org.apache.commons.exec.*
import org.springframework.beans.factory.annotation.Value
import rx.Scheduler
import rx.functions.Action0
import rx.schedulers.Schedulers

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap
import java.util.function.ToDoubleFunction

@Slf4j
class JobExecutorLocal implements JobExecutor {

  @Value('${terraform.jobs.local.timeoutMinutes:10}')
  long timeoutMinutes

  Scheduler scheduler = Schedulers.computation()
  Map<String, Map> jobIdToHandlerMap = new ConcurrentHashMap<String, Map>()

  @Override String startJob(JobRequest jobRequest) {
    log.info("Starting job: $jobRequest.maskedTokenizedCommand...")
    String jobId = jobRequest.jobId

    scheduler.createWorker().schedule(
      new Action0() {
        @Override void call() {
          ByteArrayOutputStream stdOutAndErr = new ByteArrayOutputStream()
          PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(stdOutAndErr)
          CommandLine commandLine

          if (jobRequest.tokenizedCommand) {
            log.info("Executing $jobId with tokenized command: $jobRequest.maskedTokenizedCommand")

            // Grab the first element as the command.
            commandLine = new CommandLine(jobRequest.tokenizedCommand[0])

            // Treat the rest as arguments.
            String[] arguments = Arrays.copyOfRange(jobRequest.tokenizedCommand.toArray(), 1, jobRequest.tokenizedCommand.size())

            commandLine.addArguments(arguments, false)
          } else {
            log.info("No tokenizedCommand specified for $jobId.")

            throw new IllegalArgumentException("No tokenizedCommand specified for $jobId.")
          }

          DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler()
          ExecuteWatchdog watchdog = new ExecuteWatchdog(timeoutMinutes * 60 * 1000){
            @Override
            void timeoutOccured(Watchdog w) {
              // If a watchdog is passed in, this was an actual time-out. Otherwise, it is likely
              // the result of calling watchdog.destroyProcess().
              if (w) {
                log.info("Job $jobId timed-out (after $timeoutMinutes minutes).")

                cancelJob(jobId)
              }

              super.timeoutOccured(w)
            }
          }
          Executor executor = new DefaultExecutor()
          executor.setStreamHandler(pumpStreamHandler)
          executor.setWatchdog(watchdog)
          executor.execute(commandLine, resultHandler)

          // Give the job some time to spin up.
          sleep(500)

          jobIdToHandlerMap.put(jobId, [
            handler: resultHandler,
            watchdog: watchdog,
            stdOutAndErr: stdOutAndErr
          ])
        }
      }
    )

    return jobId
  }

  @Override boolean jobExists(String jobId) {
    return jobIdToHandlerMap.containsKey(jobId)
  }

  @Override JobStatus updateJob(String jobId) {
    try {
      log.info("Polling state for $jobId...")

      if (jobIdToHandlerMap[jobId]) {
        JobStatus jobStatus = new JobStatus(id: jobId)

        DefaultExecuteResultHandler resultHandler
        ByteArrayOutputStream stdOutAndErr

        jobIdToHandlerMap[jobId].with {
          resultHandler = it.handler
          stdOutAndErr = it.stdOutAndErr
        }

        String logsContent = new String(stdOutAndErr.toByteArray())

        if (resultHandler.hasResult()) {
          log.info("State for $jobId changed with exit code $resultHandler.exitValue.")

          if (!logsContent) {
            logsContent = resultHandler.exception ? resultHandler.exception.message : "No output from command."
          }

          if (resultHandler.exitValue == 0) {
            jobStatus.state = JobStatus.State.COMPLETED
            jobStatus.result = JobStatus.Result.SUCCESS
          } else {
            jobStatus.state = JobStatus.State.CANCELED
            jobStatus.result = JobStatus.Result.FAILURE
          }

          jobIdToHandlerMap.remove(jobId)
        } else {
          jobStatus.state = JobStatus.State.RUNNING
        }

        if (logsContent) {
          jobStatus.logsContent = logsContent
        }

        return jobStatus
      } else {
        // This instance of rosco is not managing the job.
        return null
      }
    } catch (Exception e) {
      log.error("Failed to update $jobId", e)

      return null
    }

  }

  @Override
  void cancelJob(String jobId) {
    log.info("Canceling job $jobId...")

    // Remove the job from this rosco instance's handler map.
    def canceledJob = jobIdToHandlerMap.remove(jobId)

    // Terminate the process.
    canceledJob?.watchdog?.destroyProcess()

    // The next polling interval will be unable to retrieve the job status and will mark the bake as canceled.
  }

  @Override
  int runningJobCount() {
    return jobIdToHandlerMap.keySet().size()
  }

}
