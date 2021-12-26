
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

CREATE TABLE IF NOT EXISTS BLACK_LIST (
  ID bigint(5) NOT NULL AUTO_INCREMENT,
  COUNT bigint(5) NOT NULL,
  USER_ID bigint(5) NOT NULL,
  PRIMARY KEY (ID),
  foreign key (USER_ID) references USER(ID)
);
