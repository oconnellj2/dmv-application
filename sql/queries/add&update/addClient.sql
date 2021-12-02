/* Add */
/* first name, last name */
insert into customer values (customer_seq.nextval, '%s', '%s');

/* Delete */
delete from customer 
where cust_id = %s;