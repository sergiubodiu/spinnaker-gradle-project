package vportfolio.batch.output.processor.batch.writer;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.PortfolioStatisticsVO;

@Component
@StepScope
public class PortfolioStatisticsWriter extends JdbcBatchItemWriter<PortfolioStatisticsVO> {

  public PortfolioStatisticsWriter(DataSource dataSource,
      @Value("#{jobParameters['executionId']}") Long executionId,
      @Value("#{jobParameters['model']}") String model) {
    setDataSource(dataSource);
    setSql("INSERT INTO portfolioStatistics (executionId, model, riskGroup, expectedLoss,"
        + "standardDeviation, OEP_50_000_Percent, OEP_75_000_Percent, OEP_80_000_Percent,"
        + "OEP_90_000_Percent, OEP_95_000_Percent, OEP_96_000_Percent, OEP_96_667_Percent,"
        + "OEP_98_000_Percent, OEP_99_000_Percent, OEP_99_500_Percent, OEP_99_600_Percent,"
        + "OEP_99_800_Percent, OEP_99_900_Percent, OEP_99_980_Percent, OEP_99_990_Percent,"
        + "OEP_100_000_Percent, AEP_50_000_Percent, AEP_75_000_Percent, AEP_80_000_Percent,"
        + "AEP_90_000_Percent, AEP_95_000_Percent, AEP_96_000_Percent, AEP_96_667_Percent,"
        + "AEP_98_000_Percent, AEP_99_000_Percent, AEP_99_500_Percent, AEP_99_600_Percent,"
        + "AEP_99_800_Percent, AEP_99_900_Percent, AEP_99_980_Percent, AEP_99_990_Percent,"
        + "AEP_100_000_Percent, TVaR_50_000_Percent, TVaR_75_000_Percent, TVaR_80_000_Percent,"
        + "TVaR_90_000_Percent, TVaR_95_000_Percent, TVaR_96_000_Percent, TVaR_96_667_Percent,"
        + "TVaR_98_000_Percent, TVaR_99_000_Percent, TVaR_99_500_Percent, TVaR_99_600_Percent,"
        + "TVaR_99_800_Percent, TVaR_99_900_Percent, TVaR_99_980_Percent, TVaR_99_990_Percent,"
        + "TVaR_100_000_Percent) "
        + "VALUES(" + executionId  + ", '" + model + "', :riskGroup, :expectedLoss,"
        + ":standardDeviation, :OEP_50_000_Percent, :OEP_75_000_Percent, :OEP_80_000_Percent,"
        + ":OEP_90_000_Percent, :OEP_95_000_Percent, :OEP_96_000_Percent, :OEP_96_667_Percent,"
        + ":OEP_98_000_Percent, :OEP_99_000_Percent, :OEP_99_500_Percent, :OEP_99_600_Percent,"
        + ":OEP_99_800_Percent, :OEP_99_900_Percent, :OEP_99_980_Percent, :OEP_99_990_Percent,"
        + ":OEP_100_000_Percent, :AEP_50_000_Percent, :AEP_75_000_Percent, :AEP_80_000_Percent,"
        + ":AEP_90_000_Percent, :AEP_95_000_Percent, :AEP_96_000_Percent,"
        + ":AEP_96_667_Percent, :AEP_98_000_Percent, :AEP_99_000_Percent, :AEP_99_500_Percent, "
        + ":AEP_99_600_Percent, :AEP_99_800_Percent, :AEP_99_900_Percent, :AEP_99_980_Percent, "
        + ":AEP_99_990_Percent, :AEP_100_000_Percent, :TVaR_50_000_Percent, :TVaR_75_000_Percent, "
        + ":TVaR_80_000_Percent, :TVaR_90_000_Percent, :TVaR_95_000_Percent, :TVaR_96_000_Percent, "
        + ":TVaR_96_667_Percent, :TVaR_98_000_Percent, :TVaR_99_000_Percent, :TVaR_99_500_Percent, "
        + ":TVaR_99_600_Percent, :TVaR_99_800_Percent, :TVaR_99_900_Percent, :TVaR_99_980_Percent, "
        + ":TVaR_99_990_Percent, :TVaR_100_000_Percent)");
    setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
  }
}
