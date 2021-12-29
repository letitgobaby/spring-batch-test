package com.batch.test.batch;

import java.time.LocalDateTime;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class ScheduleBatch {
  
  private final MainJobBuilder mainJob;
  private final JobLauncher jobLauncher;

  @Scheduled(fixedDelay = (1 * 30000))
  public void runCreateMsgJob() throws Exception {
    JobParameters jobParameters = new JobParametersBuilder()
    .addString("create-msg-job", String.valueOf(System.currentTimeMillis()))
    .toJobParameters();

    jobLauncher.run(mainJob.createJob(), jobParameters);
  }

  @Scheduled(fixedDelay = (1 * 10000))
  public void runMainJob() throws Exception {
    JobParameters jobParameters = new JobParametersBuilder()
    .addString("filter-msg-job", String.valueOf(System.currentTimeMillis()))
    .addString("startTime", LocalDateTime.now().minusSeconds(20).toString())
    .toJobParameters();

    jobLauncher.run(mainJob.mainJob(), jobParameters);
  }

}
