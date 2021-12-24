package com.batch.test.batch.steps;

import java.util.HashMap;

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

  public Step filterBlackList() {
    return stepBuilderFactory.get("black-list-step")
    .<Message, BlackList>chunk(100)
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
    jpaPagingItemReader.setPageSize(CHUNK_SIZE);

    return jpaPagingItemReader;
  }


  // TODO: Chunk 단위 중복삽입 체크
  @Bean
  public ItemProcessor<Message, BlackList> itemProcessor() {
    HashMap<Long, BlackList> map = new HashMap<>();
    return message -> {
      User user = message.getUser();
      BlackList black = blackListRepo.findByUserId(user.getId());
      
      if (black != null) {
        black.setCount(black.getCount()+ 1);
        return black;

      } else if (map.containsKey(user.getId())) {
        BlackList obj = map.get(user.getId());
        obj.setCount(obj.getCount() + 1);
        map.put(user.getId(), obj);
      }

      BlackList blackList = new BlackList();
      blackList.setCount(1L);
      blackList.setUser(user);
      map.put(user.getId(), blackList);

      return blackList;
    };
  }

  @Bean
  public ItemWriter<BlackList> jpaPagingItemWriter() {
    JpaItemWriter<BlackList> jpaItemWriter = new JpaItemWriter<>();
    jpaItemWriter.setEntityManagerFactory(entityManagerFactory);

    return jpaItemWriter;
  }


  
}
