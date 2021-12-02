select cust_id, first_name, last_name, issue_date, expiration_date 
from Permit 
where expiration_date = %s

select cust_id, first_name, last_name, issue_date, expiration_date 
from License
where expiration_date = %s

select cust_id, first_name, last_name, issue_date, expiration_date 
from Registration
where expiration_date = %s

select cust_id, first_name, last_name, issue_date, expiration_date 
from StateId
where expiration_date = %s