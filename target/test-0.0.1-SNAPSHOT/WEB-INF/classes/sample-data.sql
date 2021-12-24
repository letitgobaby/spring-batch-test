
CREATE TABLE IF NOT EXISTS USER ( 
  ID bigint(5) NOT NULL AUTO_INCREMENT, 
  NAME varchar(100) NOT NULL, 
  PRIMARY KEY (ID) 
); 

CREATE TABLE IF NOT EXISTS MESSAGE ( 
  ID bigint(5) NOT NULL AUTO_INCREMENT,
  CONTENT varchar(255) NOT NULL,
  USER_ID bigint(5) NOT NULL,
  PRIMARY KEY (ID), 
  foreign key (USER_ID) references USER(ID)
); 

INSERT INTO USER (ID, NAME) 
VALUES (1, 'kim'), (2, 'lee'), (3, 'park'), (4, 'choi'), (5, 'jeong');



