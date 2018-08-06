package sgrc.orca.terraform.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator

import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
@PropertySource("classpath:/batch-h2.properties")
class TerraformDataSourceConfiguration {
	
	@Autowired
	private Environment environment

  @Autowired
	private ResourceLoader resourceLoader

  @PostConstruct
	protected void initialize() {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator()
    populator.addScript(resourceLoader.getResource(environment.getProperty("batch.schema.script")))
    populator.setContinueOnError(true)
    DatabasePopulatorUtils.execute(populator , dataSource())
  }

	@Bean(destroyMethod="shutdown")
  DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.ignoreFailedDrops(true)
				.addScripts("classpath:org/springframework/batch/core/schema-drop-h2.sql",
						"classpath:org/springframework/batch/core/schema-h2.sql",
						"classpath:schema-all.sql")
				.build()
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

}