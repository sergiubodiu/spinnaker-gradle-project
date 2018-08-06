package sgrc.orca.terraform.jobs

import groovy.util.logging.Slf4j
import org.springframework.batch.item.ItemProcessor

@Slf4j
class InstanceItemProcessor implements ItemProcessor<Instance, Instance> {

  @Override
  Instance process(Instance item) throws Exception {
    final String name = item.getName().toUpperCase();

    final Instance transformedPerson = new Instance(name: name);

    log.info("Converting (" + item + ") into (" + transformedPerson + ")");
    return transformedPerson
  }
}
