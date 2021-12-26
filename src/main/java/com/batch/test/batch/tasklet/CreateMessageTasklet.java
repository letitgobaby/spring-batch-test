package com.batch.test.batch.tasklet;

import java.util.List;
import java.util.Random;

import com.batch.test.entity.Message;
import com.batch.test.entity.User;
import com.batch.test.repository.MessageRepo;
import com.batch.test.repository.UserRepo;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;


public class CreateMessageTasklet implements Tasklet, StepExecutionListener {
  
  private final int TEST_CREATE_MESSAGE_SIZE = 20;
  private List<User> users;
  private MessageRepo messageRepo;
  private UserRepo userRepo;

  @Autowired
  public CreateMessageTasklet(MessageRepo messageRepo, UserRepo userRepo) {
    this.messageRepo = messageRepo;
    this.userRepo = userRepo;
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    System.out.println("** beforeStep **");
    users = userRepo.findAll();
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
    System.out.println("** executeStep **");
    Random random = new Random();
    for (int i = 0; i < TEST_CREATE_MESSAGE_SIZE; i++) {
      String randomString = String.valueOf(random.nextInt());
      User user = users.get(random.nextInt(users.size()));

      Message message = new Message(randomString, user);
      messageRepo.save(message);
    }

    return RepeatStatus.FINISHED;
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    System.out.println("** afterStepStep **");
    return ExitStatus.COMPLETED;
  }

}
