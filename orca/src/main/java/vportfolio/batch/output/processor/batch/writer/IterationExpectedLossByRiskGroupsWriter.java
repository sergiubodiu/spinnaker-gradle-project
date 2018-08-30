package vportfolio.batch.output.processor.batch.writer;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.IterationExpectedLossByRiskGroupsVO;

@Component
@StepScope
public class IterationExpectedLossByRiskGroupsWriter
    extends JdbcBatchItemWriter<IterationExpectedLossByRiskGroupsVO> {

  public IterationExpectedLossByRiskGroupsWriter(DataSource dataSource,
      @Value("#{jobParameters['executionId']}") Long executionId,
      @Value("#{jobParameters['model']}") String model) {
    setDataSource(dataSource);
    setSql("INSERT INTO iterationExpectedLossByRiskGroups (executionId, model, iterationId,"
        + "sequenceId, eventId, expectedLoss, reinstatementPremium, riskGroup, fullRip)"
        + "VALUES("  + executionId  + ", '" + model + "', :iterationId, :sequenceId, :eventId, :expectedLoss,"
        + ":reinstatementPremium, :riskGroup, :fullRip)");
    setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
  }
}
