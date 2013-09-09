select * from ACCOUNT order by path, name;


select count(*) from PATTERN;

select * from PATTERN order by pattern asc;

select  count(*)   from PATTERN group by account_id;

select count(*) from ACCOUNT;

select * from ACCOUNT where name like 'Ejers%' order by path;
update ACCOUNT set REGULAR = 'N' where id = 151;

select a.id, a.parent_id, a.path, a.name, pm.pattern from ACCOUNT as a, PATTERN pm where a.id = pm.account_id order by path, name;

select * from LINE l, ACCOUNT a where l.account_id = a.id order by l.exp_date;

-- delete from LINE;
-- delete from ACCOUNT;

select * from LINE_UNMATCHED;

select * from vw_ua;

select * from vw_ap order by path;

select * from LINE where exp_text like '%ou%' order by EXP_DATE asc;

select id, parent_id, path, name, level, avg, regular from ACCOUNT order by parent_id, level asc ;

select avg, regular , count(*) from ACCOUNT group by avg, regular;

select * from USERT;


DELETE FROM PATTERN where account_id in (SELECT * FROM (SELECT a.id as id FROM ACCOUNT a, PATTERN p where a.id = p.account_id and a.id =2 ) as PA )