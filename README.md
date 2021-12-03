# DMV-Application

Implements a two-tier client-server architecture of a DMV application.

## Compilation and Execution Instructions

1. Launch `sqlpl` from the `sql/` directory:

	```bash
	sqlpl username@oracle.aloe
	```

	Run the following command to setup and populate the tabels:

	```sql
	@ maketables
	```

2. Add JDBC to the classpath:

	```bash
	$ export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
	````

3. Compile and run the program like so:

	```bash
	$ javac Prog4.java
	$ java Prog4 <username> <password>
	```

## Workload Distribution

James O'Connell - oconnellj2@email.arizona.edu

- E-R Diagram / Design.

- Queries.

- JDBC Front-End.

Danny Ryngler - dryngler@email.arizona.edu

- E-R Diagram / Design.

- Queries.

- Database Back-End.
