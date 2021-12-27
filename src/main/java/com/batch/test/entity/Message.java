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

@Entity
@Table(name = "MESSAGE")
public class Message extends DateEntity {
  
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "CONTENT")
  private String content;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_ID", referencedColumnName = "ID", nullable=true)
  private User user;

  public Message() {}
  public Message(String content, User user) {
    this.content = content;
    this.user = user;
  }

  public Long getId() {
    return this.id;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String toString() {
    return "[ Message = id: " + this.id.toString() + ", content : " + this.content + ", user :" + this.user.toString() + " ]";
  }

}
