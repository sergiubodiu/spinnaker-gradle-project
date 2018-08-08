package sgrc.orca.terraform

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator

import javax.annotation.PostConstruct
import javax.sql.DataSource

@SpringBootApplication
class Main {

//  @Autowired
//  private Environment environment
//
//  @Autowired
//  private ResourceLoader resourceLoader
//
//  @Autowired
//  private DataSource dataSource
//
//  @PostConstruct
//  protected void initialize() {
//    ResourceDatabasePopulator populator = new ResourceDatabasePopulator()
//    populator.addScript(resourceLoader.getResource(environment.getProperty("batch.schema.script")))
//    populator.setContinueOnError(true)
//    DatabasePopulatorUtils.execute(populator , dataSource)
//  }

  static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args)
  }
}
