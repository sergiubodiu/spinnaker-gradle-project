package sgrc.orca.terraform.controllers

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.StepExecution
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory
import org.springframework.boot.web.servlet.server.ServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
class WebConfig extends WebMvcConfigurationSupport {

  @Bean
  ServletWebServerFactory servletWebServerFactory(){
    return new TomcatServletWebServerFactory()
  }

	@Override
  void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		for (HttpMessageConverter<?> httpMessageConverter : converters) {
			if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
				httpMessageConverter.objectMapper.addMixIn(StepExecution.class, StepExecutionJacksonMixIn.class)
      }
		}
	}
}

/**
 * Jackson MixIn for {@link StepExecution} serialization. This MixIn excludes the {@link JobExecution} from being
 * serialized. This is due to the fact that it would cause a {@link StackOverflowError} due to a circular reference.
 */
abstract class StepExecutionJacksonMixIn {

	@JsonIgnore
	abstract JobExecution getJobExecution()
}