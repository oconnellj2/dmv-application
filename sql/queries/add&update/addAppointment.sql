/*Add Appointment*/

insert into Appointment values (appointment_seq.nextval, "Customer Id", "Service Id", to_date('JAN-18-2021', 'MON-DD-YYYY'), 1);
/*int app_id = select max(app_id) from appointment;*/
/*int lifeTime = select years_valid from service where service_id = "SERVICE ID"; */
/*int fee = select years_valid from service where service_id = "SERVICE ID"; */
insert into Xact values (xact_seq.nextval, app_id, "FEE");
insert into Document values (document_seq.nextval, "SERVICE ID", "CUSTOMER ID", to_date('JAN-18-2021', 'MON-DD-YYYY'), to_date('JAN-18-2021', 'MON-DD-YYYY'));


/*Delete Appointment*/
delete from Appointment
where app_id = %s;

delete from Xact
where app_id = %s;


/*Update appointment*/
update Appointment
set app_date = '01/14/2021'
set success = 1
where app_id = 1;


