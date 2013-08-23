use economy;

drop table LINE_UNMATCHED;
drop table LINE;
drop table PATTERN_MATCH;
drop table ACCOUNT;

CREATE TABLE ACCOUNT (
  ID int NOT NULL,
  PARENT_ID int,
  TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  TYPE varchar(5) not NULL,
  NAME varchar(400) not NULL,
   PRIMARY KEY (id)
  ) ENGINE=INNODB;


 create table PATTERN_MATCH (
     ID int NOT NULL AUTO_INCREMENT,
    TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ACCOUNT_ID int not null ,
    PATTERN varchar(300) not null,
     PRIMARY KEY (ID),
     INDEX IDX_ACC (ACCOUNT_ID),
    FOREIGN KEY (ACCOUNT_ID)  REFERENCES ACCOUNT(ID)  ON DELETE CASCADE
  ) ENGINE=INNODB;



CREATE TABLE LINE (
  ID int NOT NULL AUTO_INCREMENT,
    ACCOUNT_ID int not null ,

  TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  EXP_OWNER varchar(30) not NULL,
  EXP_ORIGIN varchar(50) not NULL,

  EXP_DATE timestamp not null,
  EXP_TEXT varchar(300) not null,
  EXP_AMOUNT decimal (10,2) not null,
  PRIMARY KEY (id),
  UNIQUE KEY exp_unique (exp_owner,EXP_ORIGIN,exp_date,exp_text,exp_amount),
  FOREIGN KEY (ACCOUNT_ID)
        REFERENCES ACCOUNT(ID)
        ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE LINE_UNMATCHED (
  ID int NOT NULL AUTO_INCREMENT,
  ACCOUNT_ID int ,

  TS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  EXP_OWNER varchar(30) not NULL,
  EXP_ORIGIN varchar(50) not NULL,

  EXP_DATE timestamp not null,
  EXP_TEXT varchar(300) not null,
  EXP_AMOUNT decimal (10,2) not null,
  PRIMARY KEY (id)

) ENGINE=INNODB;

