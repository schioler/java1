use economy;
select v.cat_name, v.group_name, v.acc_name, year(exp_date) as y, month(exp_date) as month, sum(l.exp_amount) as exp
from VW_CAT_GRP_ACC v, LINE l
where v.acc_id = l.account_id and   month(exp_date)  = 9
group by v.cat_name, v.group_name, v.acc_name,y,month
order by y,month,v.cat_name,v.group_name,v.acc_name ;