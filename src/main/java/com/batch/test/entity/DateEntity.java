package com.batch.test.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class DateEntity {
  
  @CreationTimestamp
  @Column(name = "CREATE_DATE")
  private LocalDateTime createDate;

  @UpdateTimestamp
  @Column(name = "UPDATE_DATE")
  private LocalDateTime updateDate;

  public LocalDateTime getCreateDate() {
    return this.createDate;
  }

  public LocalDateTime getUpdateDate() {
    return this.updateDate;
  }

}
