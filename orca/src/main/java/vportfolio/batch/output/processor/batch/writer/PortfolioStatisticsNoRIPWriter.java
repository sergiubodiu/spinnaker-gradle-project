package vportfolio.batch.output.processor.batch.writer;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.PortfolioStatisticsNoRIPVO;

@Component
@StepScope
public class PortfolioStatisticsNoRIPWriter extends JdbcBatchItemWriter<PortfolioStatisticsNoRIPVO> {

  public PortfolioStatisticsNoRIPWriter(DataSource dataSource,
      @Value("#{jobParameters['executionId']}") Long executionId,
      @Value("#{jobParameters['model']}") String model) {
    setDataSource(dataSource);
    setSql("INSERT INTO portfolioStatisticsNoRIP (executionId, model, eventId, deterministicLoss,"
        + "expectedLoss, standardDeviation, TEMP_50_000_Percent, TEMP_80_000_Percent, TEMP_90_000_Percent,"
        + "TEMP_96_000_Percent, TEMP_98_000_Percent, TEMP_99_000_Percent, TEMP_99_500_Percent, TEMP_99_600_Percent,"
        + "TEMP_99_900_Percent, TEMP_99_980_Percent, TEMP_99_990_Percent) "
        + "VALUES(" + executionId  + ", '" + model + "', :eventId, :deterministicLoss,"
        + ":expectedLoss, :standardDeviation, :TEMP_50_000_Percent, :TEMP_80_000_Percent, :TEMP_90_000_Percent,"
        + ":TEMP_96_000_Percent, :TEMP_98_000_Percent, :TEMP_99_000_Percent, :TEMP_99_500_Percent, :TEMP_99_600_Percent,"
        + ":TEMP_99_900_Percent, :TEMP_99_980_Percent, :TEMP_99_990_Percent)");
    setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
  }
}
