/* Add */
/* jid, did, first_name, last_name */
insert into Employee values (employee_seq.nextval, 1, 1, %s, %s); 

/* Delete */
delete from Employee
where eid = %s;

UPDATE Job
SET salaray = "%s"
WHERE jid = "%s";