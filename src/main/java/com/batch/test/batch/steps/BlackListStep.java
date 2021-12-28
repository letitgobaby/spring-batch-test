package com.batch.test.batch.steps;

import java.util.Optional;

import javax.persistence.EntityManagerFactory;

import com.batch.test.entity.BlackList;
import com.batch.test.entity.Message;
import com.batch.test.entity.User;
import com.batch.test.repository.BlackListRepo;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;

public class BlackListStep {

  private StepBuilderFactory stepBuilderFactory;
  private EntityManagerFactory entityManagerFactory;
  private BlackListRepo blackListRepo;

  private final int CHUNK_SIZE = 100;

  public BlackListStep(
    StepBuilderFactory stepBuilderFactory,
    EntityManagerFactory entityManagerFactory,
    BlackListRepo blackListRepo
  ) {
    this.stepBuilderFactory = stepBuilderFactory;
    this.entityManagerFactory = entityManagerFactory;
    this.blackListRepo = blackListRepo;
  }


  public Step stepOn() {
    System.out.println("\n\n");
    System.out.println("hello!!!!");
    System.out.println("\n\n");
    return stepBuilderFactory.get("black-list-step")
    .<Message, BlackList>chunk(1) // this config for transaction size(i mean chunk size) of itemWriter
    .reader(jpaItemReader())
    .processor(itemProcessor())
    .writer(jpaPagingItemWriter())
    .build();
  }


  private JpaPagingItemReader<Message> jpaItemReader() {
    JpaPagingItemReader<Message> jpaPagingItemReader = new JpaPagingItemReader<Message>();
    jpaPagingItemReader.setQueryString("select m from Message m where m.content < 1");
    jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
    jpaPagingItemReader.setPageSize(CHUNK_SIZE);  // this config for paging size of itemReader

    return jpaPagingItemReader;
  }

  private ItemProcessor<Message, BlackList> itemProcessor() {
    return message -> {
      User user = message.getUser();

      // update entity
      Optional<BlackList> black = blackListRepo.findByUserId(user.getId());
      if (black.isPresent()) {
        BlackList entity = black.get();
        entity.setCount(entity.getCount()+ 1);
        return entity;
      }

      // create new entity
      BlackList newBlackObj = new BlackList();
      newBlackObj.setCount(1L);
      newBlackObj.setUser(user);
      return newBlackObj;
    };
  }

  private ItemWriter<BlackList> jpaPagingItemWriter() {
    JpaItemWriter<BlackList> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

    return jpaItemWriter;
  }

}
