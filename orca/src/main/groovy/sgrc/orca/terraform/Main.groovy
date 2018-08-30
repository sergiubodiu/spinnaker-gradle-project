package sgrc.orca.terraform

import org.springframework.boot.SpringApplication
import org.springframework.boot.SpringBootConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import sgrc.orca.terraform.config.BatchConfiguration
import sgrc.orca.terraform.controllers.WebConfig

import javax.sql.DataSource

@SpringBootConfiguration
@Import([BatchConfiguration, WebConfig])
@ComponentScan(basePackageClasses = Main)
class Main {

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

  //	@Bean(destroyMethod="close")
//	public DataSource dataSource() {
//		HikariDataSource dataSource = new HikariDataSource();
//		dataSource.setDriverClassName(environment.getProperty("batch.jdbc.driver"));
//		dataSource.setUrl(environment.getProperty("batch.jdbc.url"));
//		dataSource.setUsername(environment.getProperty("batch.jdbc.user"));
//		dataSource.setPassword(environment.getProperty("batch.jdbc.password"));
//		return dataSource;
//	}

  static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args)
  }
}
