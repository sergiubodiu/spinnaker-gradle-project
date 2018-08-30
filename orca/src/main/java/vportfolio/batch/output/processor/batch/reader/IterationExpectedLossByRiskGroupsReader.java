package vportfolio.batch.output.processor.batch.reader;

import java.io.File;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.IterationExpectedLossByRiskGroupsVO;

@Component
@StepScope
public class IterationExpectedLossByRiskGroupsReader
    extends FlatFileItemReader<IterationExpectedLossByRiskGroupsVO> {

  public IterationExpectedLossByRiskGroupsReader(
      @Value("#{jobParameters['workingDirectory']}") String workingDirectory,
      @Value("#{jobParameters['model']}") String model,
      @Value("${pricing-engine.file.iteration-expected-loss-by-risk-groups}") String fileName) {
    setName(IterationExpectedLossByRiskGroupsReader.class.getSimpleName());
    setResource(new FileSystemResource(
        workingDirectory + File.separator +
            model + File.separator +
            fileName));
    setLinesToSkip(2);
    setLineMapper(new DefaultLineMapper<IterationExpectedLossByRiskGroupsVO>(){{
      setLineTokenizer(new DelimitedLineTokenizer(){{
        setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
        setNames(new String[]{"iterationId", "sequenceId", "eventId", "expectedLoss",
            "reinstatementPremium", "riskGroup", "fullRip"});
        setFieldSetMapper(new BeanWrapperFieldSetMapper<IterationExpectedLossByRiskGroupsVO>(){{
          setTargetType(IterationExpectedLossByRiskGroupsVO.class);
        }});
      }});
    }});
  }
}
