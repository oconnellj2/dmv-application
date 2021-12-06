/* Danny Permit Appointment */
insert into Appointment values (appointment_seq.nextval, 1, 1, to_date('JAN-15-2021', 'MON-DD-YYYY'), 1);
insert into Xact values (xact_seq.nextval, 1, 7);
insert into Document values (document_seq.nextval, 1, 1, to_date('JAN-15-2021', 'MON-DD-YYYY'), to_date('JAN-15-2022', 'MON-DD-YYYY'));
/*insert into Permit values (permit_seq.nextval, 1, to_date('JAN-15-2021', 'MON-DD-YYYY'), to_date('JAN-15-2022', 'MON-DD-YYYY'));*/

/* James License Appointment */
insert into Appointment values (appointment_seq.nextval, 2, 2, to_date('JAN-16-2021', 'MON-DD-YYYY'), 1);
insert into Xact values (xact_seq.nextval, 2, 25);
insert into Document values (document_seq.nextval, 2, 2, to_date('JAN-16-2021', 'MON-DD-YYYY'), to_date('JAN-16-2033', 'MON-DD-YYYY'));
/*insert into License values (license_seq.nextval, 2, to_date('JAN-16-2021', 'MON-DD-YYYY'), to_date('JAN-16-2033', 'MON-DD-YYYY'));*/

/* Sourav Registration Appointment */
insert into Appointment values (appointment_seq.nextval, 4, 3, to_date('JAN-17-2021', 'MON-DD-YYYY'), 1);
insert into Xact values (xact_seq.nextval, 3, 100);
insert into Document values (document_seq.nextval, 3, 4, to_date('JAN-17-2021', 'MON-DD-YYYY'), to_date('JAN-17-2022', 'MON-DD-YYYY'));
/*insert into Registration values (registration_seq.nextval, 4, to_date('JAN-17-2021', 'MON-DD-YYYY'), to_date('JAN-17-2022', 'MON-DD-YYYY'));*/

/* Justin State Id Appointment */
insert into Appointment values (appointment_seq.nextval, 5, 4, to_date('JAN-18-2021', 'MON-DD-YYYY'), 1);
insert into Xact values (xact_seq.nextval, 4, 12);
insert into Document values (document_seq.nextval, 4, 5, to_date('JAN-18-2021', 'MON-DD-YYYY'), to_date('JAN-18-2041', 'MON-DD-YYYY'));
/*insert into StateId values (stateid_seq.nextval, 5, to_date('JAN-18-2021', 'MON-DD-YYYY'), to_date('JAN-18-2041', 'YYYY-MON-DD'));*/

insert into Appointment values (appointment_seq.nextval, 1, 1, to_date('JAN-18-2021', 'MON-DD-YYYY'), 1);
/*int app_id = select max(app_id) from appointment;*/
/*int lifeTime = select years_valid from service where service_id = "SERVICE ID"
insert into Xact values (xact_seq.nextval, app_id, 5);
insert into Document values (document_seq.nextval, 1, 1, to_date('JAN-18-2021', 'MON-DD-YYYY'), to_date('JAN-18-2021', 'MON-DD-YYYY'));