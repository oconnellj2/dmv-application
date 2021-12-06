select c.cust_id, first_name, last_name, issue_date, expiration_date 
from document d, customer c
where d.cust_id = c.cust_id and 
service_id = 4 and 
expiration_date < to_date('01/18/2050', 'MM/DD/YYYY'); 