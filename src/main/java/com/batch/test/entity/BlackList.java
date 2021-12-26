package com.batch.test.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.ToString;

@ToString
@Entity
@Table(name = "BLACK_LIST")
public class BlackList {
  
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "COUNT")
  private Long count;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable = true)
  private User user;

  public BlackList() {}
  public BlackList(User user, Long count) {
    this.user = user;
    this.count = count;
  }

  public Long getId() {
    return this.id;
  }

  public Long getCount() {
    return this.count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  } 

}
