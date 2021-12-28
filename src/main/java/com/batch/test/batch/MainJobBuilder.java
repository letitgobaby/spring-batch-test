package com.batch.test.batch;

import javax.persistence.EntityManagerFactory;

import com.batch.test.batch.steps.BlackListStep;
import com.batch.test.batch.steps.MessageStep;
import com.batch.test.repository.BlackListRepo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MainJobBuilder {
    
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;
  private final MessageStep msgStep;

  private final BlackListRepo blackListRepo;

  @Bean
  public Job mainJob() {
    return jobBuilderFactory.get("filter-job")
      .start(getFilterBlackList())
      .build();
  }

  @Bean
  public Job createJob() {
    return jobBuilderFactory.get("main-job")
      .start(msgStep.CreateMsgStep())
      .build();
  }

  private Step getFilterBlackList() {
    return new BlackListStep(
      stepBuilderFactory, 
      entityManagerFactory, 
      blackListRepo
      ).stepOn();
  }

}
