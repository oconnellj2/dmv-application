SET pagesize 500
SET linesize 500
SET define off
SET autocommit off

drop table Customer;
drop table Appointment;
drop table Xact;
drop table Service;
drop table Document;
-- drop table Permit;
-- drop table License;
-- drop table Registration;
-- drop table StateId; 
drop table Job; 
drop table Employee;
drop table Department;

drop sequence customer_seq;
drop sequence appointment_seq;
drop sequence xact_seq;
drop sequence service_seq;
drop sequence document_seq;
-- drop sequence permit_seq;
-- drop sequence license_seq;
-- drop sequence registration_seq;
-- drop sequence stateId_seq;
drop sequence job_seq;
drop sequence employee_seq;
drop sequence department_seq;

CREATE TABLE Customer (
	cust_id number not null,
	first_name varchar2(50),
	last_name varchar2(50),
	primary key(cust_id)
);

CREATE TABLE Appointment (
	app_id number not null,
	cust_id number,
	service_id number, 
	app_date date,
	success number,
	primary key(app_id)
);

CREATE TABLE Xact (
	xact_id number not null,
	app_id number,
	fee number not null,
	primary key(xact_id)
);

CREATE TABLE Service (
	service_id number not null,
	name varchar2(100),
	fee number,
	years_valid number,
	primary key(service_id)
);

CREATE TABLE Document (
	docid number not null,
	service_id number, 
	cust_id number,
	issue_date date not null,
	expiration_date date not null,
	primary key(docid)
);

/*
CREATE TABLE Permit (
	pid number not null,
	cust_id number,
	issue_date date not null,
	expiration_date date not null,
	primary key(pid)
);

CREATE TABLE License (
	lid number not null,
	cust_id number,
	issue_date date not null,
	expiration_date date not null,
	primary key(lid)
);

CREATE TABLE Registration (
	rid number not null,
	cust_id number,
	issue_date date not null,
	expiration_date date not null,
	primary key(rid)
);

CREATE TABLE StateId (
	stid number not null,
	cust_id number,
	issue_date date not null,
	expiration_date date not null,
	primary key(stid)
);
*/

CREATE TABLE Job (
	jid number not null,
	title varchar2(100),
	salary number,
	primary key(jid)
);

CREATE TABLE Employee (
	eid number not null,
	jid number,
	did number,
	first_name varchar2(50),
	last_name varchar2(50),
	primary key(eid)
);

CREATE TABLE Department (
	did number not null,
	name varchar2(100) not null
);


CREATE SEQUENCE customer_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE appointment_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE xact_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE service_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE document_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

/*
CREATE SEQUENCE permit_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE license_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE registration_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE stateId_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;
*/

CREATE SEQUENCE job_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE employee_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

CREATE SEQUENCE department_seq
  MINVALUE 1
  START WITH 1
  INCREMENT BY 1
  CACHE 20;

GRANT SELECT ON Customer TO PUBLIC;
GRANT SELECT ON Appointment TO PUBLIC;
GRANT SELECT ON Xact TO PUBLIC;
GRANT SELECT ON Service TO PUBLIC;
-- GRANT SELECT ON Permit TO PUBLIC;
-- GRANT SELECT ON License TO PUBLIC;
-- GRANT SELECT ON Registration TO PUBLIC;
-- GRANT SELECT ON StateId TO PUBLIC;
GRANT SELECT ON Job TO PUBLIC;
GRANT SELECT ON Employee TO PUBLIC;
GRANT SELECT ON Department TO PUBLIC;

@ insertCustomer
@ insertService
@ insertEmployeeAndDepartment
@ insertAppointment