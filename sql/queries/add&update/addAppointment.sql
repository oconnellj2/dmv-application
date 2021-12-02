
/* Add Appointment */

/* cust_id number,
	service_id number - 1 for permit, 2 for license, 3 for registration, 4 for StateId
	app_date date,
	success number,
	primary key(app_id)
*/

/* 	
    xact_id number not null,
	app_id number,
	fee number not null,
*/
insert into Appointment values (appointment_seq.nextval, %s, %s, %s, %s);
insert into Xact values (xact_seq.nextval, %s, %s);

/* 	cust_id number,
	issue_date date not null,
	expiration_date date not null,
*/
insert into Permit values (permit_seq.nextval, %s, %s, %s);
insert into License values (license_seq.nextval, %s, %s, %s);
insert into StateId values (license_seq.nextval, %s, %s, %s);


/* Delete Appointment */

/* Pass customer id and app_id */

select service_id from Appointment
where cust_id = %s and app_id = %s;

select name from Service
where service_id = service_id; /* service id above */

delete from Appointment
where cust_id = %s;

delete from Xact
where app_id = %s; /* App Id from appointment query */

delete from service_id /* above */
where cust_id = %s;


