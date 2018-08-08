package sgrc.orca.terraform.config

import org.springframework.batch.core.configuration.annotation.BatchConfigurer
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import sgrc.orca.terraform.MdcThreadPoolTaskExecutor

import javax.sql.DataSource

@Configuration
public class BatchConfiguration {

    @Bean(destroyMethod="shutdown")
  DataSource dataSource() {
    return new EmbeddedDatabaseBuilder()
        .setType(EmbeddedDatabaseType.H2)
        .ignoreFailedDrops(true)
//        .addScripts("classpath:org/springframework/batch/core/schema-drop-h2.sql",
//        "classpath:org/springframework/batch/core/schema-h2.sql",
//        "classpath:schema-all.sql")
        .build()
  }

  @Bean
  BatchConfigurer configurer(){
    return new DefaultBatchConfigurer(dataSource())
  }

//	@Bean(destroyMethod="close")
//	public DataSource dataSource() {
//		HikariDataSource dataSource = new HikariDataSource();
//		dataSource.setDriverClassName(environment.getProperty("batch.jdbc.driver"));
//		dataSource.setUrl(environment.getProperty("batch.jdbc.url"));
//		dataSource.setUsername(environment.getProperty("batch.jdbc.user"));
//		dataSource.setPassword(environment.getProperty("batch.jdbc.password"));
//		return dataSource;
//	}

  @Bean
  TaskExecutor taskExecutor(BatchConfigurationProperties batchConfig) {
    ThreadPoolTaskExecutor taskExecutor = new MdcThreadPoolTaskExecutor()
    taskExecutor.setCorePoolSize(batchConfig.getTaskExecutor().getCorePoolSize())
    taskExecutor.setQueueCapacity(batchConfig.getTaskExecutor().getQueueCapacity())
    taskExecutor.setMaxPoolSize(batchConfig.getTaskExecutor().getMaxPoolSize())
    taskExecutor.afterPropertiesSet()
    return taskExecutor
  }
}
