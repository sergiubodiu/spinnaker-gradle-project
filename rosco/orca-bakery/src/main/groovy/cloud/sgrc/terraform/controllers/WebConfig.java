package cloud.sgrc.terraform.controllers;

import java.util.List;

import cloud.sgrc.terraform.config.BaseConfiguration;
import cloud.sgrc.terraform.config.BatchWebAutoConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.jsr.launch.JsrJobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	@Autowired
	private BaseConfiguration baseConfig;

	@Autowired
	private BatchWebAutoConfiguration batchWebAutoConfiguration;

	@Autowired(required = false)
	private JsrJobOperator jsrJobOperator;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		for (HttpMessageConverter<?> httpMessageConverter : converters) {
			if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
				final MappingJackson2HttpMessageConverter converter = (MappingJackson2HttpMessageConverter) httpMessageConverter;
				converter.getObjectMapper().addMixIn(StepExecution.class, StepExecutionJacksonMixIn.class);
			}
		}
	}

	@Bean
	public JobMonitoringController jobMonitoringController() {
		return new JobMonitoringController(baseConfig.jobOperator(), baseConfig.jobExplorer(),
				batchWebAutoConfiguration.runningExecutionTracker());
	}

	@Bean
	public JobOperationsController jobOperationsController() {
		return new JobOperationsController(baseConfig.jobOperator(), baseConfig.jobExplorer(), baseConfig.jobRegistry(),
				baseConfig.jobRepository(), baseConfig.jobLauncher(), jsrJobOperator);
	}

}

/**
 * Jackson MixIn for {@link StepExecution} serialization. This MixIn excludes the {@link JobExecution} from being
 * serialized. This is due to the fact that it would cause a {@link StackOverflowError} due to a circular reference.
 */
abstract class StepExecutionJacksonMixIn {

	@JsonIgnore
	abstract JobExecution getJobExecution();
}