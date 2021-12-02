/* Danny Permit Appointment */
insert into Appointment values (appointment_seq.nextval, 1, 1, to_date('2021-JAN-15', 'YYYY-MON-DD'), 1);
insert into Xact values (xact_seq.nextval, 1, 7);
insert into Permit values (permit_seq.nextval, 1, to_date('2021-JAN-15', 'YYYY-MON-DD'), to_date('2022-JAN-15', 'YYYY-MON-DD'));

/* James License Appointment */
insert into Appointment values (appointment_seq.nextval, 2, 2, to_date('2021-JAN-16', 'YYYY-MON-DD'), 1);
insert into Xact values (xact_seq.nextval, 2, 25);
insert into License values (license_seq.nextval, 2, to_date('2021-JAN-16', 'YYYY-MON-DD'), to_date('2033-JAN-16', 'YYYY-MON-DD'));

/* Sourav Registration Appointment */
insert into Appointment values (appointment_seq.nextval, 4, 3, to_date('2021-JAN-17', 'YYYY-MON-DD'), 1);
insert into Xact values (xact_seq.nextval, 3, 100);
insert into License values (license_seq.nextval, 2, to_date('2021-JAN-17', 'YYYY-MON-DD'), to_date('2022-JAN-17', 'YYYY-MON-DD'));

/* Justin State Id Appointment */
insert into Appointment values (appointment_seq.nextval, 5, 4, to_date('2021-JAN-18', 'YYYY-MON-DD'), 1);
insert into Xact values (xact_seq.nextval, 4, 12);
insert into License values (license_seq.nextval, 2, to_date('2021-JAN-18', 'YYYY-MON-DD'), to_date('2041-JAN-18', 'YYYY-MON-DD'));
