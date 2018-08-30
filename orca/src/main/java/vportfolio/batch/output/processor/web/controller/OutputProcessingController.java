package vportfolio.batch.output.processor.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vportfolio.batch.BatchJobLauncher;
import vportfolio.batch.config.ApiConfig;
import vportfolio.batch.output.processor.vo.OutputFilesParsingRequest;

@RestController
@RequestMapping(ApiConfig.API_PATH)
public class OutputProcessingController {

  private BatchJobLauncher batchJobLauncher;

  @Autowired
  public OutputProcessingController(BatchJobLauncher batchJobLauncher) {
    this.batchJobLauncher = batchJobLauncher;
  }

  @PostMapping("/process/outputs")
  public void processPricingEngineOutputsJob(
      @RequestBody OutputFilesParsingRequest outputFilesParsingRequest) throws Exception {
    batchJobLauncher
        .processPricingEngineOutputsJob(outputFilesParsingRequest.getExecutionId(),
            outputFilesParsingRequest.getWorkingDirectory(),
            outputFilesParsingRequest.getModel());
  }

  @GetMapping("/process/outputs")
  public void processPricingEngineOutputsJob(
      @RequestParam(value = "executionId") Long executionId,
      @RequestParam(value = "workingDirectory") String workingDirectory,
      @RequestParam(value = "model") String model) throws Exception {
    batchJobLauncher.processPricingEngineOutputsJob(executionId,
        workingDirectory,
        model);
  }
}
