package vportfolio.batch.output.processor.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.PricingOutputVO;

@Component
public class PricingOutputProcessor implements ItemProcessor<PricingOutputVO, PricingOutputVO> {

  public static final String ERROR = "Error";

  @Override
  public PricingOutputVO process(final PricingOutputVO pricingOutputVO) {
    pricingOutputVO.setCompleted(!pricingOutputVO.getStarted().contains(ERROR));
    return pricingOutputVO;
  }
}
