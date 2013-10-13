select * from ACCOUNT order by path, name;


select count(*) from PATTERN;

select u.name, a.path, a.name, p.pattern from PATTERN p, ACCOUNT a, USERT u where p.account_id = a.id and a.user_id = u.id and u.name = 'lars.schioler'  order by a.path, a.name, p.pattern asc;

select  count(*)   from PATTERN group by account_id;

select count(*) from ACCOUNT;

select * from ACCOUNT where name = 'Bolig' ;

update ACCOUNT set regular = 'N' where id = 14;

select a.id, a.parent_id, a.path, a.name, pm.pattern from ACCOUNT as a, PATTERN pm where a.id = pm.account_id order by a.path, a.name;

select * from LINE l, ACCOUNT a where l.account_id = a.id order by l.exp_date;

delete from LINE where user_id = 6
-- user_id in (select id from USERT where name = 'lars.schioler')
-- delete from LINE where account_id = 287;
delete from ACCOUNT where user_id=6;

select * from LINE_UNMATCHED lu, USERT u where u.id = lu.user_id and u.name ='lars.schioler';

select * from LINE l where account_id = 490 order by EXP_DATE;


select * from vw_ua;

select * from vw_ap where path like '%Rådighed/Tøj%' ;

select * from LINE where exp_text like '%ou%' order by EXP_DATE asc;

select id, parent_id, path, name, level, avg, regular from ACCOUNT order by parent_id, level asc ;

select avg, regular , count(*) from ACCOUNT group by avg, regular;

select * from USERT;

-- insert into USERT (name) values ('danske-test');
--insert into USERT (name) values ('kirsten.schioler');

DELETE FROM PATTERN where account_id in (SELECT * FROM (SELECT a.id as id FROM ACCOUNT a, PATTERN p where a.id = p.account_id and a.id =2 ) as PA )

insert into USERT (name) values ('lars.schioler')