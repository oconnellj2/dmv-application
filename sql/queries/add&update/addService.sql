/* Add */
/* name fee years active */
insert into service values (service_seq.nextval, %s, %s, %s);

/* Delete */
delete from service
where name = %s;