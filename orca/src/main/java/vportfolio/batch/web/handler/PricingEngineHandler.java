package vportfolio.batch.web.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import vportfolio.batch.web.vo.PricingEngineRequest;

@Component
public class PricingEngineHandler {

  @Value("${pricing-engine.file.instruction.name}")
  private String instructionFileName;
  private RestTemplate restTemplate;
  private static final Logger LOG = LoggerFactory.getLogger(PricingEngineHandler.class);
  private static final String PRICING_ENGINE_URL = "http://localhost:8080/api/v1/pricing/execute/async";

  @Autowired
  public PricingEngineHandler(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public void callPricingEngine(long executionId, String workingDirectory, String model) {

    HttpEntity<PricingEngineRequest> httpEntity = new HttpEntity<>(
        new PricingEngineRequest(instructionFileName, workingDirectory, executionId, model));

    LOG.debug("Invoking pricing engine job now...");

    String response = restTemplate
        .postForObject(PRICING_ENGINE_URL, httpEntity, String.class);

    LOG.debug("Pricing engine successfully invoked.");
  }
}
