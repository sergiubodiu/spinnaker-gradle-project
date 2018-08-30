package vportfolio.batch.config;

import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

/**
 * Swagger Configuration Class Swagger UI Url: http://[domain][/app-root]/swagger-ui.html
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  private static final AuthorizationScope[] NO_AUTHORIZATION_SCOPES = new AuthorizationScope[0];
  private static final String ACCESS_TOKEN_NAME = "access_token";
  @Value("${swagger.title}")
  private String title;
  @Value("${swagger.description}")
  private String description;
  @Value("${swagger.version}")
  private String version;
  @Value("${swagger.terms-of-service-url}")
  private String termsOfServiceUrl;
  @Value("${swagger.license.type-version}")
  private String licenseTypeVersion;
  @Value("${swagger.license.url}")
  private String licenseUrl;
  @Value("${swagger.contact.name}")
  private String contactName;
  @Value("${swagger.contact.url}")
  private String contactUrl;
  @Value("${swagger.contact.email}")
  private String contactEmail;

  @Bean
  public Docket vPortfolioDefinitionApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(
            Predicates.or(
              regex(ApiConfig.API_PATH + ".*"),
              regex("/health")))
        .build()
        .apiInfo(vPortfolioDefinitionApiMetaData())
        .securitySchemes(Arrays.asList(apiKey()))
        .securityContexts(Arrays.asList(securityContext()));
  }

  private ApiInfo vPortfolioDefinitionApiMetaData() {
    return new ApiInfo(
        title,
        description,
        version,
        termsOfServiceUrl,
        new Contact(contactName, contactUrl, contactEmail),
        licenseTypeVersion,
        licenseUrl,
        new ArrayList<>()
    );
  }

  private ApiKey apiKey() {
    return new ApiKey(ACCESS_TOKEN_NAME, "Authorization", ApiKeyVehicle.HEADER.getValue());
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(securityReferences())
        .forPaths(regex(ApiConfig.API_PATH + ".*"))
        .build();
  }

  private List<SecurityReference> securityReferences() {
    // We do not care about swagger authorization scopes or any additional 'features' of swagger
    return Arrays.asList(new SecurityReference(ACCESS_TOKEN_NAME, NO_AUTHORIZATION_SCOPES));
  }

}