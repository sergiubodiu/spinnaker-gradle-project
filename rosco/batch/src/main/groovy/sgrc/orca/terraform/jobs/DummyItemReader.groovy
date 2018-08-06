package sgrc.orca.terraform.jobs

import groovy.util.logging.Slf4j
import org.springframework.batch.item.ItemReader

/**
 * {@link ItemReader} with hard-coded input data.
 */
@Slf4j
class DummyItemReader implements ItemReader<String> {

	private String[] input = ["Good", "morning!", "This", "is", "your", "ItemReader", "speaking!" ]

	private int index = 0

	@Override
	String read() throws Exception {
		if (index < input.length) {
			String item = input[index++]
			log.info(item)
			return item
		} else {
			return null
		}
	}
}