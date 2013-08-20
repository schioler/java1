create or replace view VW_ACCOUNT_LINE as
select l.exp_owner, a.cat_name,a.group_name,a.acc_name,l.exp_date,l.exp_text,l.exp_amount 
from LINE l, VW_CAT_GRP_ACC a 
where l.account_id = a.acc_id 
order by l.exp_date;
