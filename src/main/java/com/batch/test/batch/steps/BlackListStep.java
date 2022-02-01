package com.batch.test.batch.steps;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.batch.test.entity.BlackList;
import com.batch.test.entity.Message;
import com.batch.test.entity.User;
import com.batch.test.repository.BlackListRepo;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BlackListStep {

  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;
  private final BlackListRepo blackListRepo;
  private final int CHUNK_SIZE = 100;

  private final EntityManager entityManager;

  @Bean
  @JobScope
  public Step stepOn() {
    return stepBuilderFactory.get("blackList-filter-step")
    .<Message, BlackList>chunk(CHUNK_SIZE) // this config for transaction size(i mean chunk size) of itemWriter
    .reader(jpaItemReader(null))
    .processor(itemProcessor())
    .writer(jpaPagingItemWriter())
    .build();
  }


  @Bean
  @StepScope
  public JpaPagingItemReader<Message> jpaItemReader(@Value("#{jobParameters[startTime]}") String startTime) {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("startTime", LocalDateTime.parse(startTime));

    JpaPagingItemReader<Message> jpaPagingItemReader = new JpaPagingItemReader<Message>();
    jpaPagingItemReader.setQueryString("select m from Message m where m.content < 1 and m.createDate > :startTime");
    jpaPagingItemReader.setParameterValues(parameters);
    jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
    jpaPagingItemReader.setPageSize(CHUNK_SIZE);  // this config for paging size of itemReader
    
    return jpaPagingItemReader;
  }

  @Bean
  public ItemProcessor<Message, BlackList> itemProcessor() {
    return message -> {
      User user = message.getUser();
      BlackList upsertObject;

      // update entity
      Optional<BlackList> black = blackListRepo.findByUserId(user.getId());
      if (black.isPresent()) {
        BlackList entity = black.get();
        black.get().setCount(entity.getCount()+ 1);
        upsertObject = entity;
      } else {
        // create new entity
        BlackList newBlackObj = new BlackList();
        newBlackObj.setCount(1L);
        newBlackObj.setUser(user);
        upsertObject = newBlackObj;
      }

      entityManager.persist(upsertObject);
      return upsertObject;
    };
  }

  @Bean
  public ItemWriter<BlackList> jpaPagingItemWriter() {
    JpaItemWriter<BlackList> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

    return jpaItemWriter;
  }

}
