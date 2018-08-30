package vportfolio.batch.output.processor.batch.reader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.stereotype.Component;
import vportfolio.batch.output.processor.vo.PricingOutputVO;

@Component
@StepScope
public class PricingOutputReader extends StaxEventItemReader<PricingOutputVO> {

  public PricingOutputReader(
      @Value("#{jobParameters['workingDirectory']}") String workingDirectory,
      @Value("#{jobParameters['model']}") String model,
      @Value("${pricing-engine.file.pricing-output}") String fileName) {
    setName(PricingOutputReader.class.getSimpleName());
    setResource(new FileSystemResource(
        workingDirectory + File.separator +
            model + File.separator +
            fileName));
    setFragmentRootElementName("result");

    Map<String, String> aliases = new HashMap<>();
    aliases.put("result", "vportfolio.batch.output.processor.vo.PricingOutputVO");

    XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
    xStreamMarshaller.setAliases(aliases);
    setUnmarshaller(xStreamMarshaller);
  }
}
