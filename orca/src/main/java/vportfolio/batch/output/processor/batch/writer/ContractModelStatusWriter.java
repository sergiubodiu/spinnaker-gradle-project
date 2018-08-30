package vportfolio.batch.output.processor.batch.writer;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.ContractModelStatusVO;

@Component
@StepScope
public class ContractModelStatusWriter extends JdbcBatchItemWriter<ContractModelStatusVO> {

  public ContractModelStatusWriter(DataSource dataSource,
      @Value("#{jobParameters['executionId']}") Long executionId,
      @Value("#{jobParameters['model']}") String model) {
    setDataSource(dataSource);
    setSql("INSERT INTO contractModelStatus (executionId, model, contractId, modeledStatus,"
        + "expectedLoss, path) "
        + "VALUES("  + executionId  + ", '" + model + "', :contractId, :modeledStatus, :expectedLoss, :path)");
    setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
  }
}
