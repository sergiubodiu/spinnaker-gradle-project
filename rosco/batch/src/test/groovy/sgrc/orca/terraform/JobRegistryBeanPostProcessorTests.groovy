/*
 * Copyright 2018 the original author or authors.
 */
package sgrc.orca.terraform

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail

import java.util.Collection

import org.junit.Test
import org.springframework.batch.core.configuration.DuplicateJobException
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor
import org.springframework.batch.core.configuration.support.MapJobRegistry
import org.springframework.beans.FatalBeanException
import org.springframework.context.support.ClassPathXmlApplicationContext

/**
 * @author Sergiu Bodiu
 * 
 */
class JobRegistryBeanPostProcessorTests {

	private JobRegistryBeanPostProcessor processor = new JobRegistryBeanPostProcessor()

	@Test
  void testBeforeInitialization() throws Exception {
		// should be a no-op
		assertEquals("foo", processor.postProcessBeforeInitialization("foo", "bar"))
  }

	@Test
  void testAfterInitializationWithWrongType() throws Exception {
		// should be a no-op
		assertEquals("foo", processor.postProcessAfterInitialization("foo", "bar"))
  }

	@Test
  void testAfterInitializationWithCorrectType() throws Exception {
		MapJobRegistry registry = new MapJobRegistry()
    processor.setJobRegistry(registry)
    JobSupport job = new JobSupport()
    job.setBeanName("foo")
    assertNotNull(processor.postProcessAfterInitialization(job, "bar"))
    assertEquals("[foo]", registry.getJobNames().toString())
  }

	@Test
  void testAfterInitializationWithGroupName() throws Exception {
		MapJobRegistry registry = new MapJobRegistry()
    processor.setJobRegistry(registry)
    processor.setGroupName("jobs")
    JobSupport job = new JobSupport()
    job.setBeanName("foo")
    assertNotNull(processor.postProcessAfterInitialization(job, "bar"))
    assertEquals("[jobs.foo]", registry.getJobNames().toString())
  }

	@Test
  void testAfterInitializationWithDuplicate() throws Exception {
		MapJobRegistry registry = new MapJobRegistry()
    processor.setJobRegistry(registry)
    JobSupport job = new JobSupport()
    job.setBeanName("foo")
    processor.postProcessAfterInitialization(job, "bar")
    try {
			processor.postProcessAfterInitialization(job, "spam")
      fail("Expected FatalBeanException")
    }
		catch (FatalBeanException e) {
			// Expected
			assertTrue(e.getCause() instanceof DuplicateJobException)
    }
	}

	@Test
  void testUnregisterOnDestroy() throws Exception {
		MapJobRegistry registry = new MapJobRegistry()
    processor.setJobRegistry(registry)
    JobSupport job = new JobSupport()
    job.setBeanName("foo")
    assertNotNull(processor.postProcessAfterInitialization(job, "bar"))
    processor.destroy()
    assertEquals("[]", registry.getJobNames().toString())
  }

}