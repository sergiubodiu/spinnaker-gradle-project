package sgrc.orca.terraform.jobs

import groovy.util.logging.Slf4j
import org.springframework.batch.item.ItemWriter

@Slf4j
class DummyItemWriter implements ItemWriter<Object> {

	@Override
	public void write(List<? extends Object> data) throws Exception {
		log.info("{}", data);
	}

}