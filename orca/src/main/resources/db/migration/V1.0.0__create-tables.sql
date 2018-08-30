CREATE TABLE contractModelStatus (
  executionId   BIGINT,
  model         VARCHAR(50),
  contractId    BIGINT,
  modeledStatus VARCHAR(100),
  expectedLoss  DECIMAL(20, 4),
  path          VARCHAR(500)
);

CREATE TABLE portfolioStatistics (
  executionId           BIGINT,
  model                 VARCHAR(50),
  riskGroup             VARCHAR(100),
  expectedLoss          DECIMAL(20, 4),
  standardDeviation     DECIMAL(20, 4),
  OEP_50_000_Percent    DECIMAL(20, 4),
  OEP_75_000_Percent    DECIMAL(20, 4),
  OEP_80_000_Percent    DECIMAL(20, 4),
  OEP_90_000_Percent    DECIMAL(20, 4),
  OEP_95_000_Percent    DECIMAL(20, 4),
  OEP_96_000_Percent    DECIMAL(20, 4),
  OEP_96_667_Percent    DECIMAL(20, 4),
  OEP_98_000_Percent    DECIMAL(20, 4),
  OEP_99_000_Percent    DECIMAL(20, 4),
  OEP_99_500_Percent    DECIMAL(20, 4),
  OEP_99_600_Percent    DECIMAL(20, 4),
  OEP_99_800_Percent    DECIMAL(20, 4),
  OEP_99_900_Percent    DECIMAL(20, 4),
  OEP_99_980_Percent    DECIMAL(20, 4),
  OEP_99_990_Percent    DECIMAL(20, 4),
  OEP_100_000_Percent   DECIMAL(20, 4),
  AEP_50_000_Percent    DECIMAL(20, 4),
  AEP_75_000_Percent    DECIMAL(20, 4),
  AEP_80_000_Percent    DECIMAL(20, 4),
  AEP_90_000_Percent    DECIMAL(20, 4),
  AEP_95_000_Percent    DECIMAL(20, 4),
  AEP_96_000_Percent    DECIMAL(20, 4),
  AEP_96_667_Percent    DECIMAL(20, 4),
  AEP_98_000_Percent    DECIMAL(20, 4),
  AEP_99_000_Percent    DECIMAL(20, 4),
  AEP_99_500_Percent    DECIMAL(20, 4),
  AEP_99_600_Percent    DECIMAL(20, 4),
  AEP_99_800_Percent    DECIMAL(20, 4),
  AEP_99_900_Percent    DECIMAL(20, 4),
  AEP_99_980_Percent    DECIMAL(20, 4),
  AEP_99_990_Percent    DECIMAL(20, 4),
  AEP_100_000_Percent   DECIMAL(20, 4),
  TVaR_50_000_Percent   DECIMAL(20, 4),
  TVaR_75_000_Percent   DECIMAL(20, 4),
  TVaR_80_000_Percent   DECIMAL(20, 4),
  TVaR_90_000_Percent   DECIMAL(20, 4),
  TVaR_95_000_Percent   DECIMAL(20, 4),
  TVaR_96_000_Percent   DECIMAL(20, 4),
  TVaR_96_667_Percent   DECIMAL(20, 4),
  TVaR_98_000_Percent   DECIMAL(20, 4),
  TVaR_99_000_Percent   DECIMAL(20, 4),
  TVaR_99_500_Percent   DECIMAL(20, 4),
  TVaR_99_600_Percent   DECIMAL(20, 4),
  TVaR_99_800_Percent   DECIMAL(20, 4),
  TVaR_99_900_Percent   DECIMAL(20, 4),
  TVaR_99_980_Percent   DECIMAL(20, 4),
  TVaR_99_990_Percent   DECIMAL(20, 4),
  TVaR_100_000_Percent  DECIMAL(20, 4)
);

CREATE TABLE portfolioStatisticsRIP (
  executionId           BIGINT,
  model                 VARCHAR(50),
  eventId               BIGINT,
  deterministicLoss     DECIMAL(20, 4),
  expectedLoss          DECIMAL(20, 4),
  standardDeviation     DECIMAL(20, 4),
  TEMP_50_000_Percent   DECIMAL(20, 4),
  TEMP_80_000_Percent   DECIMAL(20, 4),
  TEMP_90_000_Percent   DECIMAL(20, 4),
  TEMP_96_000_Percent   DECIMAL(20, 4),
  TEMP_98_000_Percent   DECIMAL(20, 4),
  TEMP_99_000_Percent   DECIMAL(20, 4),
  TEMP_99_500_Percent   DECIMAL(20, 4),
  TEMP_99_600_Percent   DECIMAL(20, 4),
  TEMP_99_900_Percent   DECIMAL(20, 4),
  TEMP_99_980_Percent   DECIMAL(20, 4),
  TEMP_99_990_Percent   DECIMAL(20, 4)
);

CREATE TABLE portfolioStatisticsNoRIP (
  executionId           BIGINT,
  model                 VARCHAR(50),
  eventId               BIGINT,
  deterministicLoss     DECIMAL(20, 4),
  expectedLoss          DECIMAL(20, 4),
  standardDeviation     DECIMAL(20, 4),
  TEMP_50_000_Percent   DECIMAL(20, 4),
  TEMP_80_000_Percent   DECIMAL(20, 4),
  TEMP_90_000_Percent   DECIMAL(20, 4),
  TEMP_96_000_Percent   DECIMAL(20, 4),
  TEMP_98_000_Percent   DECIMAL(20, 4),
  TEMP_99_000_Percent   DECIMAL(20, 4),
  TEMP_99_500_Percent   DECIMAL(20, 4),
  TEMP_99_600_Percent   DECIMAL(20, 4),
  TEMP_99_900_Percent   DECIMAL(20, 4),
  TEMP_99_980_Percent   DECIMAL(20, 4),
  TEMP_99_990_Percent   DECIMAL(20, 4)
);

/*
CREATE TABLE annualExpectedLossByRiskGroups (
  executionId BIGINT,
  model       VARCHAR(50),
  iterationId BIGINT,
  allZone     DECIMAL(20, 4),
  asiaWS      DECIMAL(20, 4),
  CAEQ        DECIMAL(20, 4),
  catNonGOM   DECIMAL(20, 4),
  chinaEQ     DECIMAL(20, 4),
  europeEQ    DECIMAL(20, 4),
  GMWS        DECIMAL(20, 4),
  japanEQ     DECIMAL(20, 4),
  noncat      DECIMAL(20, 4),
  PNWEQ       DECIMAL(20, 4),
  taiwanEQ    DECIMAL(20, 4),
  USEQ        DECIMAL(20, 4)
);

CREATE TABLE contractExpectedLossByRiskGroups (
  executionId BIGINT,
  model       VARCHAR(50),
  contractId  BIGINT,
  allZone     DECIMAL(20, 4),
  asiaWS      DECIMAL(20, 4),
  CAEQ        DECIMAL(20, 4),
  catNonGOM   DECIMAL(20, 4),
  chinaEQ     DECIMAL(20, 4),
  europeEQ    DECIMAL(20, 4),
  GMWS        DECIMAL(20, 4),
  japanEQ     DECIMAL(20, 4),
  noncat      DECIMAL(20, 4),
  PNWEQ       DECIMAL(20, 4),
  taiwanEQ    DECIMAL(20, 4),
  USEQ        DECIMAL(20, 4)
);
*/

CREATE TABLE iterationExpectedLossByRiskGroups (
  executionId           BIGINT,
  model                 VARCHAR(50),
  iterationId           BIGINT,
  sequenceId            BIGINT,
  eventId               BIGINT,
  expectedLoss          DECIMAL(20, 4),
  reinstatementPremium  DECIMAL(20, 4),
  riskGroup             VARCHAR(100),
  fullRip               DECIMAL(20, 4)
);

CREATE TABLE pricingOutput (
  executionId           BIGINT,
  model                 VARCHAR(50),
  id                    BIGINT,
  message               VARCHAR(500),
  successful            TINYINT
);