   select A.ID as a_id,A.NAME,P.ID as p_id,P.PATTERN from ACCOUNT A,PATTERN_MATCH P where A.id = P.account_id;
   
   select * from LINE;