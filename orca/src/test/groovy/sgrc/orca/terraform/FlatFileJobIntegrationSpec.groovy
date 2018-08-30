package sgrc.orca.terraform

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.batch.core.ExitStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.ApplicationContext
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.Future

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT, properties = [ "server.port=8090" ])
class FlatFileJobIntegrationSpec extends Specification {


	@Shared
	@AutoCleanup
	ConfigurableApplicationContext context

	void setupSpec() {
		Future future = Executors
				.newSingleThreadExecutor().submit(
				new Callable() {
					@Override
					public ConfigurableApplicationContext call() throws Exception {
						return (ConfigurableApplicationContext) SpringApplication
								.run(Main.class)
					}
				})
		context = future.get(60, TimeUnit.SECONDS)
	}
//
//	void "should return pong from /ping"() {
//		when:
//		ResponseEntity entity = new RestTemplate().getForEntity("http://localhost:8080/ping", String.class)
//
//		then:
//		entity.statusCode == HttpStatus.OK
//		entity.body == 'pong'
//	}
//
//	private RestTemplate restTemplate = new RestTemplateBuilder().build()
//
//	@Test void testLaunchJob() throws Exception {
//		// Given
//		String jobParameters = "pathToFile=classpath:partner-import.csv"
//		// When
//		ExitStatus exitStatus = runJobAndWaitForCompletion("localhost", "8090", "flatfileJob", jobParameters)
//		// Then
//		Assert.assertEquals(ExitStatus.COMPLETED.getExitCode(), exitStatus.getExitCode())
//	}
//
//	protected ExitStatus runJobAndWaitForCompletion(String hostname, String port, String jobName, String jobParameters)
//			throws InterruptedException {
//		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>()
//		parameters.add("jobParameters", jobParameters)
//		String jobExecutionId = restTemplate.postForObject(
//				"http://" + hostname + ":" + port + "/batch/operations/jobs/" + jobName + "", parameters, String.class)
//		ExitStatus exitStatus = getStatus(hostname, port, jobExecutionId)
//		// Wait for end of job
//		while (exitStatus.isRunning()) {
//			Thread.sleep(100)
//			exitStatus = getStatus(hostname, port, jobExecutionId)
//		}
//		return exitStatus
//	}
//
//	private ExitStatus getStatus(String hostname, String port, String jobExecutionId) {
//		String jobstatus = restTemplate.getForObject(
//				"http://" + hostname + ":" + port + "/batch/operations/jobs/executions/" + jobExecutionId,
//				String.class)
//		return new ExitStatus(jobstatus)
//	}

}