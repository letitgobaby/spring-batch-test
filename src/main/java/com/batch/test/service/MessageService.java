package com.batch.test.service;

import java.util.List;

import com.batch.test.entity.User;
import com.batch.test.repository.MessageRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {
  
  private final MessageRepo messageRepo;



}
