package vportfolio.batch.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import brave.Tracer;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vportfolio.batch.BatchJobLauncher;
import vportfolio.batch.config.ApiConfig;
import vportfolio.batch.web.handler.BatchJobStatusHandler;
import vportfolio.batch.web.handler.BatchJobStepsHandler;
import vportfolio.batch.web.handler.PricingEngineHandler;
import vportfolio.batch.web.vo.BatchJobStatusRequest;
import vportfolio.batch.web.vo.BatchJobStatusResponse;
import vportfolio.batch.web.vo.BatchJobStepsRequest;
import vportfolio.batch.web.vo.BatchJobStepsResponse;

@RestController
@RequestMapping(ApiConfig.API_PATH)
public class MainBatchController {

  private static final Logger LOG = LoggerFactory.getLogger(MainBatchController.class);
  private BatchJobLauncher batchJobLauncher;
  private Tracer tracer;
  private BeanFactory beanFactory;
  private BatchJobStatusHandler batchJobStatusHandler;
  private BatchJobStepsHandler batchJobStepsHandler;
  private PricingEngineHandler pricingEngineHandler;
  private ExecutorService executorService =
      Executors.newFixedThreadPool(25, new CustomizableThreadFactory("batch-run-%d"));

  @Autowired
  public MainBatchController(BatchJobLauncher batchJobLauncher, Tracer tracer,
      PricingEngineHandler pricingEngineHandler, BatchJobStatusHandler batchJobStatusHandler,
      BatchJobStepsHandler batchJobStepsHandler) {
    this.batchJobLauncher = batchJobLauncher;
    this.tracer = tracer;
    this.pricingEngineHandler = pricingEngineHandler;
    this.batchJobStatusHandler = batchJobStatusHandler;
    this.batchJobStepsHandler = batchJobStepsHandler;
  }

  @PostMapping("/job/run")
  public void runBatch(
      @RequestParam(value = "executionId") Long executionId,
      @RequestParam(value = "workingDirectory") String workingDirectory,
      @RequestParam(value = "models") String[] models) {
    for (String model : models) {
      CompletableFuture<Boolean> completableFuture = CompletableFuture.supplyAsync(() -> {
        Boolean unsuccessful = false;
        try {
          LOG.debug("Starting batch job for execution Id %s, working directory %s and model %s",
              executionId, workingDirectory, model);
          JobExecution jobExecution = batchJobLauncher
              .generatePricingEngineInputsJob(executionId, workingDirectory, model);
          unsuccessful = jobExecution.getStatus().isUnsuccessful();
        } catch (Exception e) {
          LOG.error("Error occurred while trying to generate input files", e);
          e.printStackTrace();
        }
        return unsuccessful;
      }, new TraceableExecutorService(beanFactory, executorService,"generateInputFiles"));

      completableFuture.thenAccept(unsuccessful ->
          {
            if (!unsuccessful) {
              pricingEngineHandler
                  .callPricingEngine(executionId, workingDirectory, model);
            }
          }
      );
    }
  }

  @PostMapping("/jobs/all")
  public List<BatchJobStatusResponse> getAllJobs(
      @RequestBody BatchJobStatusRequest batchJobStatusRequest) {

    List<BatchJobStatusResponse> batchJobStatusResponses = new ArrayList<>();
    this.batchJobStatusHandler
        .findAllInputJobs(batchJobStatusResponses, batchJobStatusRequest.getIndex(),
            batchJobStatusRequest.getCount());
    this.batchJobStatusHandler
        .findAllOutputJobs(batchJobStatusResponses, batchJobStatusRequest.getIndex(),
            batchJobStatusRequest.getCount());
    this.batchJobStatusHandler.setAggregatedJobStatus(batchJobStatusResponses);
    return batchJobStatusResponses;
  }

  //TODO Jira updated at Spring to fix this bug https://jira.spring.io/browse/BATCH-2345
  @GetMapping("/jobs/running")
  public List<BatchJobStatusResponse> getAllRunningJobs() {

    List<BatchJobStatusResponse> batchJobStatusResponses = new ArrayList<>();
    this.batchJobStatusHandler.findAllRunningInputJobs(batchJobStatusResponses);
    this.batchJobStatusHandler
        .findAllRunningOutputJobs(batchJobStatusResponses);
    this.batchJobStatusHandler.setAggregatedJobStatus(batchJobStatusResponses);
    return batchJobStatusResponses;
  }

  @PostMapping("/job/status/steps")
  public BatchJobStepsResponse getJobSteps(
      @RequestBody BatchJobStepsRequest batchJobStepsRequest) {

    BatchJobStepsResponse batchJobStepsResponse = new BatchJobStepsResponse();
    this.batchJobStepsHandler
        .findBatchJobStepsResponse(batchJobStepsRequest, batchJobStepsResponse);
    return batchJobStepsResponse;
  }
}
