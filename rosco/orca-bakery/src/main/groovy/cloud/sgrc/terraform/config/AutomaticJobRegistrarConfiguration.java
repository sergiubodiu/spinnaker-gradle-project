package cloud.sgrc.terraform.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.support.AutomaticJobRegistrar;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This configuration looks for jobs in a modular fashion, meaning that every job configuration file gets its own
 * Child-ApplicationContext. Configuration files can be XML files in the location /META-INF/spring/batch/jobs,
 * overridable via property batch.config.path.xml, and JavaConfig classes in the package spring.batch.jobs, overridable
 * via property batch.config.package.javaconfig.
 *
 * Customization is done by adding a Configuration class that extends {@link AutomaticJobRegistrarConfigurationSupport}.
 * This will disable this auto configuration.
 *
 * @author Thomas Bosch
 */
@Configuration
public class AutomaticJobRegistrarConfiguration {


	private static final Logger LOGGER = LoggerFactory.getLogger(AutomaticJobRegistrarConfiguration.class);

	@Autowired
	private BatchConfigurationProperties batchConfig;

	@Autowired
	private AutomaticJobRegistrar automaticJobRegistrar;

	@PostConstruct
	public void initialize() throws Exception {
		// Default order for the AutomaticJobRegistrar is Ordered.LOWEST_PRECEDENCE. Since we want to register
		// listeners after the jobs are registered through the AutomaticJobRegistrar, we need to decrement its
		// order value by one. The creation of the AutomaticJobRegistrar bean is hidden deep in the automatic
		// batch configuration, so we unfortunately have to do it here.
		automaticJobRegistrar.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
		addApplicationContextFactories(automaticJobRegistrar);
	}

	protected void addApplicationContextFactories(AutomaticJobRegistrar automaticJobRegistrar) throws Exception {
		registerJobsFromJavaConfig(automaticJobRegistrar);
	}


	protected void registerJobsFromJavaConfig(AutomaticJobRegistrar automaticJobRegistrar)
			throws ClassNotFoundException, IOException {
		List<Class<?>> classes = findMyTypes(batchConfig.getConfig().getPackageJavaconfig());
		for (Class<?> clazz : classes) {
			LOGGER.info("Register jobs from {}", clazz);
			automaticJobRegistrar.addApplicationContextFactory(new GenericApplicationContextFactory(clazz));
		}
	}

	private List<Class<?>> findMyTypes(String basePackage) throws IOException, ClassNotFoundException {
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

		List<Class<?>> candidates = new ArrayList<>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage)
				+ "/**/*.class";
		Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
				if (isCandidate(metadataReader)) {
					candidates.add(Class.forName(metadataReader.getClassMetadata().getClassName()));
				}
			}
		}
		return candidates;
	}

	private String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}

	private boolean isCandidate(MetadataReader metadataReader) {
		try {
			Class<?> c = Class.forName(metadataReader.getClassMetadata().getClassName());
			if (c.getAnnotation(Configuration.class) != null) {
				return true;
			}
		} catch (Throwable e) {
		}
		return false;
	}

}