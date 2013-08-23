use economy;

create or replace view VW_CAT_GRP_ACC as
select
   c.id as cat_id, c.name  as cat_name, g.id as group_id, g.name as group_name, a.id as acc_id, a.name as acc_name
from
   ACCOUNT AS c, ACCOUNT AS g, ACCOUNT as a
where
   c.id = g.parent_id and g.id = a.parent_id ;

create or replace  view VW_ACCOUNT_LINE as
select l.exp_owner, a.cat_name,a.group_name,a.acc_name,l.exp_date,l.exp_text,l.exp_amount
from LINE l, VW_CAT_GRP_ACC a
where l.account_id = a.acc_id
order by l.exp_date;
