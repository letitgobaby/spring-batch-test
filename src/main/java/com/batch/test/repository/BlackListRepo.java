package com.batch.test.repository;

import java.util.Optional;

import com.batch.test.entity.BlackList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlackListRepo extends JpaRepository<BlackList, Long> {
  
  long countByUserId(long id);

  Optional<BlackList> findByUserId(Long id); 

}
