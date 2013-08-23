use economy;
create or replace view VW_CAT_GRP_ACC as
select
   c.id as cat_id, c.name  as cat_name, g.id as group_id, g.name as group_name, a.id as acc_id, a.name as acc_name
from
   ACCOUNT AS c, ACCOUNT AS g, ACCOUNT as a
where
   c.id = g.parent_id and g.id = a.parent_id ;
