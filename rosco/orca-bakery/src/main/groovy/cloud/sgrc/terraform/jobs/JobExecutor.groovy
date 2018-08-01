package cloud.sgrc.terraform.jobs

interface JobExecutor {
  String startJob(JobRequest jobRequest)
  boolean jobExists(String jobId)
  JobStatus updateJob(String jobId)
  void cancelJob(String jobId)
  int runningJobCount()
}