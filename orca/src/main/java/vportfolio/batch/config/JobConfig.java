package vportfolio.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import vportfolio.batch.enums.JobName;
import vportfolio.batch.enums.JobStep;
import vportfolio.batch.output.processor.batch.processor.PricingOutputProcessor;
import vportfolio.batch.output.processor.batch.reader.AnnualExpectedLossByRiskGroupsReader;
import vportfolio.batch.output.processor.batch.reader.ContractExpectedLossByRiskGroupsReader;
import vportfolio.batch.output.processor.batch.reader.ContractModelStatusReader;
import vportfolio.batch.output.processor.batch.reader.IterationExpectedLossByRiskGroupsReader;
import vportfolio.batch.output.processor.batch.reader.PortfolioStatisticsNoRIPReader;
import vportfolio.batch.output.processor.batch.reader.PortfolioStatisticsRIPReader;
import vportfolio.batch.output.processor.batch.reader.PortfolioStatisticsReader;
import vportfolio.batch.output.processor.batch.reader.PricingOutputReader;
import vportfolio.batch.output.processor.batch.tasklet.AnalyzeModelTasklet;
import vportfolio.batch.output.processor.batch.writer.AnnualExpectedLossByRiskGroupsWriter;
import vportfolio.batch.output.processor.batch.writer.ContractExpectedLossByRiskGroupsWriter;
import vportfolio.batch.output.processor.batch.writer.ContractModelStatusWriter;
import vportfolio.batch.output.processor.batch.writer.IterationExpectedLossByRiskGroupsWriter;
import vportfolio.batch.output.processor.batch.writer.PortfolioStatisticsNoRIPWriter;
import vportfolio.batch.output.processor.batch.writer.PortfolioStatisticsRIPWriter;
import vportfolio.batch.output.processor.batch.writer.PortfolioStatisticsWriter;
import vportfolio.batch.output.processor.batch.writer.PricingOutputWriter;
import vportfolio.batch.output.processor.vo.AnnualExpectedLossByRiskGroupsVO;
import vportfolio.batch.output.processor.vo.ContractExpectedLossByRiskGroupsVO;
import vportfolio.batch.output.processor.vo.ContractModelStatusVO;
import vportfolio.batch.output.processor.vo.IterationExpectedLossByRiskGroupsVO;
import vportfolio.batch.output.processor.vo.PortfolioStatisticsNoRIPVO;
import vportfolio.batch.output.processor.vo.PortfolioStatisticsRIPVO;
import vportfolio.batch.output.processor.vo.PortfolioStatisticsVO;
import vportfolio.batch.output.processor.vo.PricingOutputVO;

@Configuration
@EnableBatchProcessing
public class JobConfig {

  private static final int CHUNK_SIZE = 10000;
  private JobBuilderFactory jobBuilderFactory;
  private StepBuilderFactory stepBuilderFactory;

  @Autowired
  public JobConfig(JobBuilderFactory jobBuilderFactory,
      StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

  // BEGIN: Steps for PROCESS_PRICING_ENGINE_OUTPUTS
  @Bean
  public Step analyzeModelStep(AnalyzeModelTasklet analyzeModelTasklet) {
    return stepBuilderFactory.get(JobStep.ANALYZE_MODEL_STEP.getValue())
        .tasklet(analyzeModelTasklet)
        .build();
  }

  @Bean
  public Step parseContractModelStatusStep(
      ContractModelStatusReader contractModelStatusReader,
      ContractModelStatusWriter contractModelStatusWriter) {
    return stepBuilderFactory.get(JobStep.PARSE_CONTRACT_MODEL_STATUS_STEP.getValue())
        .<ContractModelStatusVO, ContractModelStatusVO>chunk(CHUNK_SIZE)
        .reader(contractModelStatusReader)
        .writer(contractModelStatusWriter)
        .build();
  }

  @Bean
  public Step parsePortfolioStatisticsStep(
      PortfolioStatisticsReader portfolioStatisticsReader,
      PortfolioStatisticsWriter portfolioStatisticsWriter) {
    return stepBuilderFactory.get(JobStep.PARSE_PORTFOLIO_STATISTICS_STEP.getValue())
        .<PortfolioStatisticsVO, PortfolioStatisticsVO>chunk(CHUNK_SIZE)
        .reader(portfolioStatisticsReader)
        .writer(portfolioStatisticsWriter)
        .build();
  }

  @Bean
  public Step parsePortfolioStatisticsRIPStep(
      PortfolioStatisticsRIPReader portfolioStatisticsRIPReader,
      PortfolioStatisticsRIPWriter portfolioStatisticsRIPWriter) {
    return stepBuilderFactory.get(JobStep.PARSE_PORTFOLIO_STATISTICS_RIP_STEP.getValue())
        .<PortfolioStatisticsRIPVO, PortfolioStatisticsRIPVO>chunk(CHUNK_SIZE)
        .reader(portfolioStatisticsRIPReader)
        .writer(portfolioStatisticsRIPWriter)
        .build();
  }

  @Bean
  public Step parsePortfolioStatisticsNoRIPStep(
      PortfolioStatisticsNoRIPReader portfolioStatisticsNoRipReader,
      PortfolioStatisticsNoRIPWriter portfolioStatisticsNoRipWriter) {
    return stepBuilderFactory.get(JobStep.PARSE_PORTFOLIO_STATISTICS_NO_RIP_STEP.getValue())
        .<PortfolioStatisticsNoRIPVO, PortfolioStatisticsNoRIPVO>chunk(CHUNK_SIZE)
        .reader(portfolioStatisticsNoRipReader)
        .writer(portfolioStatisticsNoRipWriter)
        .build();
  }

  @Bean
  public Step parseAnnualExpectedLossByRiskGroupsStep(
      AnnualExpectedLossByRiskGroupsReader annualExpectedLossByRiskGroupsReader,
      AnnualExpectedLossByRiskGroupsWriter annualExpectedLossByRiskGroupsWriter) {
    return stepBuilderFactory.get(JobStep.PARSE_ANNUAL_EXPECTED_LOSS_BY_RISK_GROUPS_STEP.getValue())
        .<AnnualExpectedLossByRiskGroupsVO, AnnualExpectedLossByRiskGroupsVO>chunk(CHUNK_SIZE)
        .reader(annualExpectedLossByRiskGroupsReader)
        .writer(annualExpectedLossByRiskGroupsWriter)
        .build();
  }

  @Bean
  public Step parseContractExpectedLossByRiskGroupsStep(
      ContractExpectedLossByRiskGroupsReader contractExpectedLossByRiskGroupsReader,
      ContractExpectedLossByRiskGroupsWriter contractExpectedLossByRiskGroupsWriter) {
    return stepBuilderFactory
        .get(JobStep.PARSE_CONTRACT_EXPECTED_LOSS_BY_RISK_GROUPS_STEP.getValue())
        .<ContractExpectedLossByRiskGroupsVO, ContractExpectedLossByRiskGroupsVO>chunk(CHUNK_SIZE)
        .reader(contractExpectedLossByRiskGroupsReader)
        .writer(contractExpectedLossByRiskGroupsWriter)
        .build();
  }

  @Bean
  public Step parseIterationExpectedLossByRiskGroupsStep(
      IterationExpectedLossByRiskGroupsReader iterationExpectedLossByRiskGroupsReader,
      IterationExpectedLossByRiskGroupsWriter iterationExpectedLossByRiskGroupsWriter) {
    return stepBuilderFactory
        .get(JobStep.PARSE_ITERATION_EXPECTED_LOSS_BY_RISK_GROUPS_STEP.getValue())
        .<IterationExpectedLossByRiskGroupsVO, IterationExpectedLossByRiskGroupsVO>chunk(CHUNK_SIZE)
        .reader(iterationExpectedLossByRiskGroupsReader)
        .writer(iterationExpectedLossByRiskGroupsWriter)
        .build();
  }

  @Bean
  public Step parsePricingOutputStep(
      PricingOutputReader pricingOutputReader,
      PricingOutputProcessor pricingOutputProcessor,
      PricingOutputWriter pricingOutputWriter) {
    return stepBuilderFactory.get(JobStep.PARSE_PRICING_OUTPUT_STEP.getValue())
        .<PricingOutputVO, PricingOutputVO>chunk(CHUNK_SIZE)
        .reader(pricingOutputReader)
        .processor(pricingOutputProcessor)
        .writer(pricingOutputWriter)
        .build();
  }
  // END: Steps for PROCESS_PRICING_ENGINE_OUTPUTS


  @Bean
  Job processPricingEngineOutputsJob(
      Step analyzeModelStep,
      Step parseContractModelStatusStep,
      Step parsePortfolioStatisticsStep,
      Step parsePortfolioStatisticsRIPStep,
      Step parsePortfolioStatisticsNoRIPStep,
      Step parseAnnualExpectedLossByRiskGroupsStep,
      Step parseContractExpectedLossByRiskGroupsStep,
      Step parseIterationExpectedLossByRiskGroupsStep,
      Step parsePricingOutputStep) {

    /*
    For now we're not really running these step since the file structure changes based on the
    contracts selected. These files are not parsed in legacy app anyways.
      parseAnnualExpectedLossByRiskGroupsStep
      parseContractExpectedLossByRiskGroupsStep
     */
    // Steps are running in parallel
    Flow flowInParallel = new FlowBuilder<Flow>("flowInParallel")
        .split(new SimpleAsyncTaskExecutor())
        .add(
            new FlowBuilder<Flow>(analyzeModelStep.getName())
                .start(analyzeModelStep)
                .on("VETIV/VHEV")
                .to(new FlowBuilder<Flow>("VETIV/VHEV")
                    .start(parsePortfolioStatisticsRIPStep)
                    .on("*").to(parsePortfolioStatisticsNoRIPStep)
                    .end()
                )
                .from(analyzeModelStep)
                .on("REGULAR")
                .to(parsePortfolioStatisticsStep)
                .end(),
            new FlowBuilder<Flow>(parseIterationExpectedLossByRiskGroupsStep.getName())
                .start(parseIterationExpectedLossByRiskGroupsStep)
                .build()
        )
        .build();

    return jobBuilderFactory.get(JobName.PROCESS_PRICING_ENGINE_OUTPUTS.toString())
        .incrementer(new RunIdIncrementer())
        .start(parsePricingOutputStep)
        .on("*")
        .to(parseContractModelStatusStep)
        .from(parseContractModelStatusStep)
        .on("*")
        .to(flowInParallel)
        .end()
        .build();
  }
}
