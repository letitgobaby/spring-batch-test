package com.batch.test.batch;

import com.batch.test.batch.steps.BlackListStep;
import com.batch.test.batch.steps.MessageStep;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MainJobBuilder {
    
  private final JobBuilderFactory jobBuilderFactory;
  private final BlackListStep blackListStep;
  private final MessageStep msgStep;

  @Bean
  public Job filterJob() {
    return jobBuilderFactory.get("filter-msg-job")
      .start(blackListStep.stepOn())
      .build();
  }

  @Bean
  public Job createJob() {
    return jobBuilderFactory.get("create-msg-job")
      .start(msgStep.CreateMsgStep())
      .build();
  }

}
