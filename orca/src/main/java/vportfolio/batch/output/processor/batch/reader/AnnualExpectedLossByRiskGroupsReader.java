package vportfolio.batch.output.processor.batch.reader;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.AnnualExpectedLossByRiskGroupsVO;

import java.io.File;

@Component
@StepScope
public class AnnualExpectedLossByRiskGroupsReader
    extends FlatFileItemReader<AnnualExpectedLossByRiskGroupsVO> {

  public AnnualExpectedLossByRiskGroupsReader(
      @Value("#{jobParameters['workingDirectory']}") String workingDirectory,
      @Value("#{jobParameters['model']}") String model,
      @Value("${pricing-engine.file.annual-expected-loss-by-risk-groups}") String fileName) {
    setName(AnnualExpectedLossByRiskGroupsReader.class.getSimpleName());
    setResource(new FileSystemResource(
        workingDirectory + File.separator +
            model + File.separator +
            fileName));
    setLinesToSkip(1);
    setLineMapper(new DefaultLineMapper<AnnualExpectedLossByRiskGroupsVO>(){{
      setLineTokenizer(new DelimitedLineTokenizer(){{
        setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
        setNames(new String[]{"iterationId", "allZone", "asiaWS", "CAEQ", "catNonGOM", "chinaEQ",
            "europeEQ", "GMWS", "japanEQ", "noncat", "PNWEQ", "taiwanEQ", "USEQ"});
        setFieldSetMapper(new BeanWrapperFieldSetMapper<AnnualExpectedLossByRiskGroupsVO>(){{
          setTargetType(AnnualExpectedLossByRiskGroupsVO.class);
        }});
      }});
    }});
  }
}
