use economy;

drop table MATCH_;
drop table LINE;
drop table FILTER;
drop table PATTERN;
drop table ACCOUNT;
drop table USER_;

CREATE TABLE USER_ (
  ID int NOT NULL AUTO_INCREMENT,
  TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  NAME varchar(300) not NULL,
  PRIMARY KEY (id),
  UNIQUE KEY user_unique (name)
) ENGINE=INNODB;

CREATE TABLE ACCOUNT (
  ID int NOT NULL AUTO_INCREMENT,
  USER_ID INT NOT NULL,
  PARENT_ID INT NOT NULL,
  TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  NAME varchar(400) not NULL,
  PATH varchar(600),
  LEVEL int not null,
  ATYPE set('R','N','E') not null,
  PRIMARY KEY (id),
  UNIQUE KEY acc_unique (user_id, name, parent_id),
  UNIQUE KEY acc_unique_path (user_id,name, path),
  FOREIGN KEY (USER_ID)  REFERENCES USER_(ID)  ON DELETE CASCADE,
  INDEX IDX_USER(USER_ID)
) ENGINE=INNODB;


 create table PATTERN (
    ID int NOT NULL AUTO_INCREMENT,
    TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    USER_ID INT NOT NULL,
    ACCOUNT_ID int not null ,
    PATTERN varchar(400) not null,
    ACCOUNT_PATH varchar(600) not null,
    PRIMARY KEY (ID),
    UNIQUE KEY acc_unique (pattern, account_id),
    INDEX IDX_ACC (ACCOUNT_ID),
    INDEX IDX_USER(USER_ID),
    FOREIGN KEY (USER_ID)  REFERENCES USER_(ID)  ON DELETE CASCADE,
    FOREIGN KEY (ACCOUNT_ID)  REFERENCES ACCOUNT(ID)  ON DELETE CASCADE
  ) ENGINE=INNODB;

  create table FILTER (
    ID int NOT NULL AUTO_INCREMENT,
    TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    USER_ID INT NOT NULL,
    ACCOUNT_ID int not null,
    ACCOUNT_PATH varchar(600) not null,
    EXP_ORIGIN varchar(50),
    EXP_DATE timestamp ,
    EXP_TEXT varchar(300),
    EXP_AMOUNT decimal (10,2) ,
    FTYPE set('I','E') not null,
    PRIMARY KEY (ID),
    INDEX IDX_ACC (ACCOUNT_ID),
    INDEX IDX_USER(USER_ID),
    FOREIGN KEY (USER_ID)  REFERENCES USER_(ID)  ON DELETE CASCADE,
    FOREIGN KEY (ACCOUNT_ID)  REFERENCES ACCOUNT(ID)  ON DELETE CASCADE
  ) ENGINE=INNODB;



CREATE TABLE LINE (
  ID int NOT NULL AUTO_INCREMENT,
  USER_ID INT NOT NULL,
  TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  EXP_ORIGIN varchar(50) not NULL,
  EXP_DATE timestamp not null,
  EXP_TEXT varchar(300) not null,
  EXP_AMOUNT decimal (10,2) not null,
  PRIMARY KEY (id),
  UNIQUE KEY exp_unique (user_id,EXP_ORIGIN,exp_date,exp_text,exp_amount),
 -- FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID) ON DELETE CASCADE,
  FOREIGN KEY (USER_ID)  REFERENCES USER_(ID)  ON DELETE CASCADE

  ) ENGINE=INNODB;

create table MATCH_ (
    ID int NOT NULL AUTO_INCREMENT,
    TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    USER_ID INT NOT NULL,
    ACCOUNT_ID int not null ,
    LINE_ID int not null ,
    PATTERN_ID int  ,
    FILTER_ID int  ,
    PRIMARY KEY (id),
    FOREIGN KEY (USER_ID)  REFERENCES USER_(ID)  ON DELETE CASCADE,
    FOREIGN KEY (ACCOUNT_ID)  REFERENCES ACCOUNT(ID)  ON DELETE CASCADE,
    FOREIGN KEY (LINE_ID)  REFERENCES LINE(ID)  ON DELETE CASCADE,
     UNIQUE KEY exp_unique (user_id,account_id, line_id)
 ) ENGINE=INNODB;



