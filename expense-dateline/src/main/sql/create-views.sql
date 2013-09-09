use economy;

create or replace  view vw_al as
select l.exp_owner, a.name,a.path, l.exp_date,l.exp_text,l.exp_amount
from LINE l, ACCOUNT a
where l.account_id = a.id
order by l.exp_date;

create or replace  view vw_ap as
select a.id as a_id, a.name, a.path, pm.id as p_id, pm.pattern, pm.account_path
from ACCOUNT a, PATTERN pm
where pm.account_id = a.id
order by a.path, a.name;

create or replace  view vw_ua as
select u.id as u_id,u.name as u_name, a.id as a_id, a.name, a.path, a.level, a.avg, a.regular
from ACCOUNT a, USERT u
where u.id = a.user_id
order by u.name, a.path;