package com.batch.test.batch.steps;

import com.batch.test.batch.tasklet.CreateMessageTasklet;
import com.batch.test.repository.MessageRepo;
import com.batch.test.repository.UserRepo;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageStep {
  
  private final StepBuilderFactory stepBuilderFactory;

  private final MessageRepo messageRepo;
  private final UserRepo userRepo;

  public Step CreateMsgStep() {
    return stepBuilderFactory.get("create-msg-step")
      .tasklet(new CreateMessageTasklet(messageRepo, userRepo))
      .build();
  }
  
}
