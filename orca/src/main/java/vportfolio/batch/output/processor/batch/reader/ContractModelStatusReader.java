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
import vportfolio.batch.output.processor.vo.ContractModelStatusVO;

@Component
@StepScope
public class ContractModelStatusReader extends FlatFileItemReader<ContractModelStatusVO> {

  public ContractModelStatusReader(
      @Value("#{jobParameters['workingDirectory']}") String workingDirectory,
      @Value("#{jobParameters['model']}") String model,
      @Value("${pricing-engine.file.contract-model-status}") String fileName) {
    setName(ContractModelStatusReader.class.getSimpleName());
    setResource(new FileSystemResource(
        workingDirectory + File.separator +
            model + File.separator +
            fileName));
    setLinesToSkip(1);
    setLineMapper(new DefaultLineMapper<ContractModelStatusVO>() {{
      setLineTokenizer(new DelimitedLineTokenizer() {{
        setDelimiter(DelimitedLineTokenizer.DELIMITER_TAB);
        setNames(new String[]{"contractId", "modeledStatus", "expectedLoss", "path"});
        setFieldSetMapper(new BeanWrapperFieldSetMapper<ContractModelStatusVO>() {{
          setTargetType(ContractModelStatusVO.class);
        }});
      }});
    }});
  }
}
