package vportfolio.batch.output.processor.batch.tasklet;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class AnalyzeModelTasklet implements Tasklet, StepExecutionListener {

  @Override
  public void beforeStep(StepExecution stepExecution) {

  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    String model = stepExecution.getJobParameters().getString("model");
    if(model.equalsIgnoreCase("VETIV") || model.equalsIgnoreCase("VHEV")) {
      return new ExitStatus("VETIV/VHEV");
    }
    return new ExitStatus("REGULAR");
  }

  @Override
  public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
      throws Exception {
    return RepeatStatus.FINISHED;
  }
}
