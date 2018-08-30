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
import vportfolio.batch.output.processor.vo.PortfolioStatisticsNoRIPVO;

@Component
@StepScope
public class PortfolioStatisticsNoRIPReader extends FlatFileItemReader<PortfolioStatisticsNoRIPVO> {

  public PortfolioStatisticsNoRIPReader(
      @Value("#{jobParameters['workingDirectory']}") String workingDirectory,
      @Value("#{jobParameters['model']}") String model,
      @Value("${pricing-engine.file.portfolio-statistics-no-rip}") String fileName) {
    setName(PortfolioStatisticsNoRIPReader.class.getSimpleName());
    setResource(new FileSystemResource(
        workingDirectory + File.separator +
            model + File.separator +
            fileName));
    setLinesToSkip(1);
    setLineMapper(new DefaultLineMapper<PortfolioStatisticsNoRIPVO>(){{
      setLineTokenizer(new DelimitedLineTokenizer(){{
        setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
        setNames(new String[]{"eventId", "deterministicLoss", "expectedLoss", "standardDeviation",
            "TEMP_50_000_Percent", "TEMP_80_000_Percent", "TEMP_90_000_ercent", "TEMP_96_000_Percent",
            "TEMP_98_000_Percent", "TEMP_99_000_Percent", "TEMP_99_500_Percent", "TEMP_99_600_Percent",
            "TEMP_99_900_Percent", "TEMP_99_980_Percent", "TEMP_99_990_Percent"});
        setFieldSetMapper(new BeanWrapperFieldSetMapper<PortfolioStatisticsNoRIPVO>(){{
          setTargetType(PortfolioStatisticsNoRIPVO.class);
        }});
      }});
    }});
  }
}
