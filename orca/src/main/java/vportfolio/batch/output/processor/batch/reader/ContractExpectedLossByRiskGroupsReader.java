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
import vportfolio.batch.output.processor.vo.ContractExpectedLossByRiskGroupsVO;

@Component
@StepScope
public class ContractExpectedLossByRiskGroupsReader
    extends FlatFileItemReader<ContractExpectedLossByRiskGroupsVO> {

  public ContractExpectedLossByRiskGroupsReader(
      @Value("#{jobParameters['workingDirectory']}") String workingDirectory,
      @Value("#{jobParameters['model']}") String model,
      @Value("${pricing-engine.file.contract-expected-loss-by-risk-groups}") String fileName) {
    setName(ContractExpectedLossByRiskGroupsReader.class.getSimpleName());
    setResource(new FileSystemResource(
        workingDirectory + File.separator +
            model + File.separator +
            fileName));
    setLinesToSkip(1);
    setLineMapper(new DefaultLineMapper<ContractExpectedLossByRiskGroupsVO>(){{
      setLineTokenizer(new DelimitedLineTokenizer(){{
        setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
        setNames(new String[]{"contractId", "allZone", "asiaWS", "CAEQ", "catNonGOM", "chinaEQ",
            "europeEQ", "GMWS", "japanEQ", "noncat", "PNWEQ", "taiwanEQ", "USEQ"});
        setFieldSetMapper(new BeanWrapperFieldSetMapper<ContractExpectedLossByRiskGroupsVO>(){{
          setTargetType(ContractExpectedLossByRiskGroupsVO.class);
        }});
      }});
    }});
  }
}
