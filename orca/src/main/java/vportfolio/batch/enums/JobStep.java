package vportfolio.batch.enums;

public enum JobStep {
  ANALYZE_MODEL_STEP("analyzeModelStep"),
  PARSE_CONTRACT_MODEL_STATUS_STEP("parseContractModelStatusStep"),
  PARSE_PORTFOLIO_STATISTICS_STEP("parsePortfolioStatisticsStep"),
  PARSE_PORTFOLIO_STATISTICS_RIP_STEP("parsePortfolioStatisticsRIPStep"),
  PARSE_PORTFOLIO_STATISTICS_NO_RIP_STEP("parsePortfolioStatisticsNoRIPStep"),
  PARSE_ANNUAL_EXPECTED_LOSS_BY_RISK_GROUPS_STEP("parseAnnualExpectedLossByRiskGroupsStep"),
  PARSE_CONTRACT_EXPECTED_LOSS_BY_RISK_GROUPS_STEP("parseContractExpectedLossByRiskGroupsStep"),
  PARSE_ITERATION_EXPECTED_LOSS_BY_RISK_GROUPS_STEP("parseIterationExpectedLossByRiskGroupsStep"),
  PARSE_PRICING_OUTPUT_STEP("parsePricingOutputStep"),
  GENERATE_PROBABILITIES_STEP("generateProbabilitiesStep"),
  GENERATE_LAYERS_STEP("generateLayersStep"),
  GENERATE_INSTRUCTIONS_STEP("generateInstructionsStep");

  private final String value;

  JobStep(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
