package vportfolio.batch.output.processor.batch.writer;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.AnnualExpectedLossByRiskGroupsVO;

@Component
@StepScope
public class AnnualExpectedLossByRiskGroupsWriter
    extends JdbcBatchItemWriter<AnnualExpectedLossByRiskGroupsVO> {

  public AnnualExpectedLossByRiskGroupsWriter(DataSource dataSource,
      @Value("#{jobParameters['executionId']}") Long executionId,
      @Value("#{jobParameters['model']}") String model) {
    setDataSource(dataSource);
    setSql("INSERT INTO annualExpectedLossByRiskGroups (executionId, model, iterationId, allZone,"
        + "asiaWS, CAEQ, catNonGOM, chinaEQ, europeEQ, GMWS, japanEQ, noncat, PNWEQ, taiwanEQ,"
        + "USEQ) "
        + "VALUES("  + executionId  + ", '" + model + "', :iterationId, :allZone, :asiaWS, :CAEQ,"
        + ":catNonGOM, :chinaEQ, :europeEQ, :GMWS, :japanEQ, :noncat, :PNWEQ, :taiwanEQ, :USEQ)");
    setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
  }
}
