/*
 * Copyright 2017 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.spinnaker.orca.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.spinnaker.kork.core.RetrySupport;
import com.netflix.spinnaker.orca.jackson.OrcaObjectMapper;
import com.netflix.spinnaker.orca.pipeline.DefaultStageDefinitionBuilderFactory;
import com.netflix.spinnaker.orca.pipeline.StageDefinitionBuilder;
import com.netflix.spinnaker.orca.pipeline.StageDefinitionBuilderFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import rx.Scheduler;
import rx.schedulers.Schedulers;

import java.time.Clock;
import java.time.Duration;
import java.util.Collection;

import static java.time.temporal.ChronoUnit.MINUTES;

@Configuration
@ComponentScan({
  "com.netflix.spinnaker.orca.pipeline",
  "com.netflix.spinnaker.orca.deprecation",
  "com.netflix.spinnaker.orca.pipeline.util",
  "com.netflix.spinnaker.orca.telemetry",
  "com.netflix.spinnaker.orca.notifications.scheduling"
})
@EnableConfigurationProperties
public class OrcaConfiguration {
  @Bean public Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean public Duration minInactivity() {
    return Duration.of(3, MINUTES);
  }

  @Bean(destroyMethod = "") public Scheduler scheduler() {
    return Schedulers.io();
  }

  @Bean public ObjectMapper mapper() {
    return OrcaObjectMapper.newInstance();
  }


  @Bean
  @ConditionalOnMissingBean(StageDefinitionBuilderFactory.class)
  public StageDefinitionBuilderFactory stageDefinitionBuilderFactory(Collection<StageDefinitionBuilder> stageDefinitionBuilders) {
    return new DefaultStageDefinitionBuilderFactory(stageDefinitionBuilders);
  }

  @Bean
  public RetrySupport retrySupport() {
    return new RetrySupport();
  }


  @Bean
  public ThreadPoolTaskExecutor applicationEventTaskExecutor() {
    ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
    threadPool.setThreadNamePrefix("events-");
    threadPool.setCorePoolSize(20);
    threadPool.setMaxPoolSize(20);
    return threadPool;
  }

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setThreadNamePrefix("scheduler-");
    scheduler.setPoolSize(10);
    return scheduler;
  }

}
