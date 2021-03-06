
CREATE TABLE IF NOT EXISTS USER ( 
  ID bigint(5) NOT NULL AUTO_INCREMENT, 
  NAME varchar(100) NOT NULL, 
  PRIMARY KEY (ID) 
); 

CREATE TABLE IF NOT EXISTS MESSAGE ( 
  ID bigint(5) NOT NULL AUTO_INCREMENT,
  CONTENT varchar(255) NOT NULL,
  USER_ID bigint(5) NOT NULL,
  CREATE_DATE DATE NOT NULL,
  UPDATE_DATE DATE,
  PRIMARY KEY (ID), 
  foreign key (USER_ID) references USER(ID)
); 

CREATE TABLE IF NOT EXISTS BLACK_LIST (
  ID bigint(5) NOT NULL AUTO_INCREMENT,
  COUNT bigint(5) NOT NULL,
  USER_ID bigint(5) NOT NULL,
  CREATE_DATE DATE NOT NULL,
  UPDATE_DATE DATE,
  PRIMARY KEY (ID),
  foreign key (USER_ID) references USER(ID)
);


-- Shedlock Table
CREATE TABLE IF NOT EXISTS shedlock (
  name VARCHAR(64),
  lock_until TIMESTAMP(3) NULL,
  locked_at TIMESTAMP(3) NULL,
  locked_by VARCHAR(255),
  PRIMARY KEY (name)
)