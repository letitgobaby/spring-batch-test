package com.batch.test.batch.steps;

import java.util.HashMap;
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
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BlackListStep {

  private final StepBuilderFactory stepBuilderFactory;
  private final EntityManagerFactory entityManagerFactory;
  private final BlackListRepo blackListRepo;
  private final int CHUNK_SIZE = 100;

  // private HashMap<Long, BlackList> map = new HashMap<>();

  public Step filterBlackList() {
    return stepBuilderFactory.get("black-list-step")
    .<Message, BlackList>chunk(1) // this config for chunk size of itemWriter
    .reader(jpaItemReader())
    .processor(itemProcessor())
    .writer(jpaPagingItemWriter())
    .build();
  }

  @Bean
  public JpaPagingItemReader<Message> jpaItemReader() {
    JpaPagingItemReader<Message> jpaPagingItemReader = new JpaPagingItemReader<Message>();
    jpaPagingItemReader.setQueryString("select m from Message m where m.content < 1");
    jpaPagingItemReader.setEntityManagerFactory(entityManagerFactory);
    jpaPagingItemReader.setPageSize(CHUNK_SIZE);  // this config for paging size of itemReader

    return jpaPagingItemReader;
  }


  // TODO: Chunk 단위 중복삽입 체크
  @Bean
  public ItemProcessor<Message, BlackList> itemProcessor() {
    return message -> {
      User user = message.getUser();
      Optional<BlackList> black = blackListRepo.findByUserId(user.getId());
      if (black.isPresent()) {
        BlackList entity = black.get();
        entity.setCount(entity.getCount()+ 1);
        return entity;
      } 

      BlackList newBlackObj = new BlackList();
      newBlackObj.setCount(1L);
      newBlackObj.setUser(user);

      return newBlackObj;
      
      
      // if (map.containsKey(user.getId())) {
      //   BlackList existedObj = map.get(user.getId());
      //   existedObj.setCount(existedObj.getCount() + 1);
      //   map.put(user.getId(), existedObj);
      //   // map.replace(key, oldValue, newValue)
      //   return existedObj;
      // } else {
      //   BlackList newBlackObj = new BlackList();
      //   newBlackObj.setCount(1L);
      //   newBlackObj.setUser(user);
      //   map.putIfAbsent(user.getId(), newBlackObj);
      //   return newBlackObj;
      // }
    };
  }

  @Bean
  public ItemWriter<BlackList> jpaPagingItemWriter() {
    JpaItemWriter<BlackList> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

    return jpaItemWriter;
  }


  
}
