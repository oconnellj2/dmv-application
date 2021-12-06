import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * @Author James O'Connell <oconnellj2@email.arizona.edu>
 * @Author Danny Ryngler <dryngler@email.arizona.edu>
 * @Course CSC 460, Fall 2021
 * @Assignment Program 4: Database Design and Implementation.
 * @Instructor L. McCann
 * @DueDate December 6th, 2021, at the beginning of class.
 * 
 * @Purpose Using JDBC, this program offers the user a text-based client user
 *          interface of a DMV api.
 * 
 * @Requirements
 *               - Basic understanding / access to the commandline.
 *               - Java 16 or earlier.
 * 
 * @Usage
 *        - Add the Oracle JDBC driver to your CLASSPATH environment variable:
 *        export
 *        CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 * 
 *        - Compile the program:
 *        $ javac Prog4.java
 * 
 *        - Finally, Run the program; where <username> and <password> are the
 *        users oracle user name and password respectivly:
 *        $ java Prog4 <username> <password>
 * 
 * @Example
 *           $ export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 *           $ javac Prog4.java
 *           $ java Prog4 <username> <password>
 */
public class Prog4 {
	// Menu that gets printed to the user.
	private static final String menu = "\n[1] List users whose Id's will expire given a date(MM/DD/YYYY).\n" +
			"[2] For the previous month, count every type of appointment and check how many of them successfully got their IDs.\n"
			+
			"[3] Disply the collected fee amount for every department for a given month(MM/YYYY).\n" +
			"[4] What is the salary of a given Employee?\n" +
			"[5] Make an appointment.\n" +
			"[6] Cancel an appointment.\n" +
			"[7] Update an appointment.\n" +
			"[8] Add a user.\n" +
			"[9] Delete a user.\n" +
			"[10] Add an employee.\n" +
			"[11] Delete an employee.\n" +
			"[12] Update an employee.\n" +
			"[13] Add a service.\n" +
			"[14] Delete a service.\n" +
			"[15] Update a service.\n" +
			"Enter 1-15 to make a query, or type exit.\n";
	// Queries.
	private static final String q1 = "select c.cust_id, first_name, last_name, issue_date, expiration_date " +
						"from document d, customer c " +
						"where d.cust_id = c.cust_id and " +
						"service_id = 4 and " +
						"expiration_date < to_date('%s', 'MM/DD/YYYY')";
	private static final String q2 = "select name, count(success) " +
						"from Appointment, Service " +
						"where Appointment.service_id = Service.service_id " +
						"and success = 0 " +
						"and app_date > app_date - 30 " +
						"group by name";
	private static final String q3 = "select dep.name, sum(fee) " +
						"from service s, department dep, document d " +
						"where d.service_id = s.service_id " +
						"and s.service_id = dep.did " +
						"and d.issue_date between to_date('%s', 'MM/YYYY') and " +
						"to_date('%s', 'MM/YYYY') " +
						"group by dep.name " +
						"order by sum(fee) desc";
	private static final String q4 = "select salary from Job, Employee " +
						"where Job.jid = Employee.jid and " +
						"Employee.first_name = '%s' and Employee.last_name = '%s'";

	/**
	 * Purpose: Print the ResultSet from a query. The column names are printed first
	 * and then each tuple thereafter.
	 * The width of columns is found with getColumnDisplaySize().
	 * Params: ResultSet answer: to be printed
	 * int[] dtypes: datatype of each column represented by bits to know whether to
	 * call getInt(), getString(), or getDouble()
	 * value in array mean -> 0 : string; 1: int; -1 : double
	 */
	private static void printResults(ResultSet answer, int[] dtypes) {
		try {
			System.out.println();
			ResultSetMetaData answermetadata = answer.getMetaData();
			for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
				String name = answermetadata.getColumnName(i);
				if (dtypes[i - 1] == 0) {
					int width = answermetadata.getColumnDisplaySize(i);
					System.out.print(name);
					for (int j = name.length(); j <= width; j++) {
						System.out.print(" ");
					}
				} else {
					System.out.print(name + "\t");
				}
			}
			System.out.println();

			while (answer.next()) {
				String tuple = "";
				for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
					int width = answermetadata.getColumnDisplaySize(i);
					if (dtypes[i - 1] == 1) {
						int value = answer.getInt(i);
						tuple += value;
						for (int j = String.valueOf(value).length(); j <= answermetadata.getColumnName(i).length()
								+ 4; j++) { // +4 for tab
							tuple += " ";
						}
					} else if (dtypes[i - 1] == 0) {
						tuple += answer.getString(i);
						for (int j = tuple.length(); j <= width; j++) {
							tuple += " ";
						}
					} else {
						double dub = Math.round(answer.getDouble(i) * 100.00) / 100.00; // round to 2 decimal places
						tuple += dub;
						for (int j = String.valueOf(dub).length(); j <= answermetadata.getColumnName(i).length()
								+ 5; j++) { // +5 for tab and 1 for decimal place in double
							tuple += " ";
						}
					}
				}
				System.out.println(tuple);
			}
		} catch (SQLException e) {
			System.err.println("SQL Exception...");
			System.err.println("Message:   " + e.getMessage());
			System.err.println("SQLState:  " + e.getSQLState());
			System.err.println("ErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Execute insert/update/delete.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param query  - String reprsents query.
	 */
	private static void execute(Connection dbconn, Statement stmt, String query) {
		try {
			// Execute query.
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
					+ "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Connects the user to the DBMS.
	 * 
	 * @param args - Command line arguments that hold the username nad password.
	 * @return The connection session with our database.
	 */
	private static Connection connect(String[] args) {
		// Handle command line args.
		String username = null;
		String password = null;
		if (args.length == 2) {
			username = args[0];
			password = args[1];
		} else {
			System.out.println("\nUsage: java Prog4 <username> <password>\n");
			System.exit(-1);
		}

		final String oracleURL = "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		// Load the (Oracle) JDBC driver by initializing its base class,
		// 'oracle.jdbc.OracleDriver'.
		try {
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.err.println("*** ClassNotFoundException:  " +
					"Error loading Oracle JDBC driver.  \n" +
					"\tPerhaps the driver is not on the Classpath?");
			System.exit(-1);
		}

		// Make and return a database connection to the user's Oracle database.
		Connection dbconn = null;
		try {
			dbconn = DriverManager.getConnection(oracleURL, username, password);
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " +
					"Could not open JDBC connection.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
		return dbconn;
	}

	/**
	 * Execute query one on the database and output results to the user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get date from the user.
	 */
	private static void query1(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get date.
		String date = null;
		while (true) {
			System.out.print("Enter date(MM/DD/YYYY): ");
			date = input.nextLine();
			DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			sdf.setLenient(false);
			try {
				sdf.parse(date);
			} catch (ParseException e) {
				System.err.println("Error:\tEnter a vaild date!");
				continue;
			}
			break;
		}
		String query = String.format(q1, date);
		try {
			// Execute query 1.
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			if (answer != null) {
				// Output data about query.
				System.out.println("CUST_ID \t FIRST_NAME \t LAST_NAME \t ISSUE_DATE \t EXPIRATION_DATE");
				while(answer.next()){
					System.out.println(answer.getString(1)+" \t "+answer.getString(2)+" \t "+answer.getString(3)+" \t "+answer.getString(4)+" \t "+answer.getString(5));
				}
			} else {
				System.out.println("No result returned from query!");
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
					+ "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

	}

	/**
	 * Execute query two on the database and output results to the user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 */
	private static void query2(Connection dbconn, Statement stmt, ResultSet answer) {
		try {
			// Execute query.
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(q2);
			if (answer != null) {
				// Output data about query.
				int[] dTypes = { 0, 0 };
				printResults(answer, dTypes);
			} else {
				System.out.println("No result returned from query!");
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
					+ "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Execute query three on the database and output results to the user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get year and district name from the user.
	 */
	private static void query3(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get date.
		String startDate = null;
		while (true) {
			System.out.print("Enter date(MM/YYYY): ");
			startDate = input.nextLine();
			DateFormat sdf = new SimpleDateFormat("MM/yyyy");
			sdf.setLenient(false);
			try {
				sdf.parse(startDate);
			} catch (ParseException e) {
				System.err.println("Error:\tEnter a vaild date!");
				continue;
			}
			break;
		}
		int endMo = Integer.parseInt(startDate.split("/")[0]);
		String endDate = String.valueOf((endMo+1)+"/"+startDate.split("/")[1]);
		// Execute query 3.
		String query = String.format(q3, startDate, endDate);
		try {
			// Execute query.
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			if (answer != null) {
				// Output data about query.
				System.out.println("DEPARTMENT_NAME \t SUM");
				while(answer.next()){
					System.out.println(answer.getString(1)+" \t "+answer.getString(2));
				}
			} else {
				System.out.println("No result returned from query!");
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
					+ "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Execute my custom query on the database and output results to the user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void query4(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user first and last name.
		System.out.print("Enter first name: ");
		String firstName = input.nextLine();
		System.out.print("Enter last name: ");
		String lastName = input.nextLine();
		String query = String.format(q4, firstName, lastName);
		try {
			// Execute query.
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			if (answer != null) {
				// Output data about query.
				int[] dTypes = { 0, 0 };
				printResults(answer, dTypes);
			} else {
				System.out.println("No result returned from query!");
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
					+ "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Execute appointment insert from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void insertApp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user id.
		String id = null;
		String service_id = null;
		while (true) {
			System.out.print("Enter user ID: ");
			id = input.nextLine();
			// Validate user id.
			try {
				Integer.parseInt(id);
				break;
			} catch (NumberFormatException e) {
				System.err.println("Error:\tInvalid user ID!");
			}
		}
		while (true) {
			System.out.print("Enter service ID: 1 for Permit, 2 for License, 3 for Registration, 4 for StateId");
			service_id = input.nextLine();
			// Validate user id.
			try {
				Integer.parseInt(service_id);
				break;
			} catch (NumberFormatException e) {
				System.err.println("Error:\tInvalid user ID!");
			}
		}
		if (service_id.equals("1")) {
				
		} else if (service_id.equals("2")) {
			
		} else if (service_id.equals("3")) {

		} else if (service_id.equals("4")) {
		
		} else {
			
		}
		// TODO: Implement insert appointment.
		String query = "";
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute appointment delete from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void deleteApp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user id.
		String id = null;
		while (true) {
			System.out.print("Enter appointment ID: ");
			id = input.nextLine();
			// Validate user id.
			try {
				Integer.parseInt(id);
				break;
			} catch (NumberFormatException e) {
				System.err.println("Error:\tInvalid appointment ID!");
			}
		}
		// Execute delete of appointment and xact.
		String delAppt = "delete from Appointment where app_id = "+id;
		execute(dbconn, stmt, delAppt);
		String delXact = "delete from Xact where app_id = "+id;
		execute(dbconn, stmt, delXact);
		
	}

	/**
	 * Execute appointment update from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void updateApp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user id.
		String id = null;
		while (true) {
			System.out.print("Enter appointment ID: ");
			id = input.nextLine();
			// Validate user id.
			try {
				Integer.parseInt(id);
				break;
			} catch (NumberFormatException e) {
				System.err.println("Error:\tInvalid appointment ID!");
			}
		}
		// Get date.
		String date = null;
		while (true) {
			System.out.print("Enter an appointment date(MM/DD/YYYY): ");
			date = input.nextLine();
			DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			sdf.setLenient(false);
			try {
				sdf.parse(date);
			} catch (ParseException e) {
				System.err.println("Error:\tEnter a vaild date!");
				continue;
			}
			break;
		}
		String query = "update Appointment set app_date = to_date('"+date+
					   "', 'MM/DD/YYYY') where app_id = "+id;
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute User insert from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void insertUser(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user first and last name.
		System.out.print("Enter first name: ");
		String firstName = input.nextLine();
		System.out.print("Enter last name: ");
		String lastName = input.nextLine();

		String query = "insert into customer values (customer_seq.nextval, " + firstName + ", " + lastName + ") ";
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute User delete from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void deleteUser(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user id.
		System.out.print("Enter user ID number: ");
		String custID = input.nextLine();

		String query = "delete from customer where cust_id = " + custID;
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute Employee insert from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void insertEmp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get job and dept id.
		System.out.print("Enter Job ID: ");
		String jobId = input.nextLine();
		System.out.print("Enter Department ID: ");
		String deptId = input.nextLine();

		String query = "insert into Employee values (employee_seq.nextval, 1, 1, " + jobId + ", " + deptId + ")";
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute Employee delete from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void deleteEmp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get employee id from user.
		System.out.print("Enter Employee ID number: ");
		String empID = input.nextLine();

		String query = "delete from Employee where eid = " + empID;
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute Employee update from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void updateEmp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get salary from user.
		String salary = null;
		while (true) {
			System.out.print("Enter a Salary: ");
			salary = input.nextLine();
			// Validate salary.
			try {
				Integer.parseInt(salary);
				break;
			} catch (NumberFormatException e) {
				System.err.println("Error:\tInvalid Salary value!");
			}
		}
		// Get job id.
		String id = null;
		while (true) {
			System.out.print("Enter job ID: ");
			id = input.nextLine();
			// Validate job id.
			try {
				Integer.parseInt(id);
				break;
			} catch (NumberFormatException e) {
				System.err.println("Error:\tInvalid appointment ID!");
			}
		}
		String query = "UPDATE Job SET salary = "+salary+" WHERE jid = "+id;
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute Service insert from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void insertSvc(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get Service name, fee and lifetime.
		System.out.print("Enter a Service name: ");
		String svcName = input.nextLine();
		System.out.print("Enter a Service fee: ");
		String fee = input.nextLine();
		System.out.print("Enter number of years vaild: ");
		String lifetime = input.nextLine();

		String query = "insert into service values (service_seq.nextval, " + svcName + ", " + fee + ", " + lifetime
				+ ")";
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute Service delete from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void deleteSvc(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get service name from user.
		System.out.print("Enter Service name: ");
		String svcName = input.nextLine();

		String query = "delete from service where name = " + svcName;
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute Service update from user.
	 * 
	 * @param dbconn - Current connection with database.
	 * @param stmt   - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input  - Scanner used to get school name from the user.
	 */
	private static void updateSvc(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// TODO: Implement update service.
	}

	/**
	 * Main Driver function which handles the connection to the DB and prompts the
	 * user for questions to ask then answered by the DB.
	 * 
	 * @param args - Command line arguments.
	 */
	public static void main(String[] args) {
		// Connect to Oracle DBMS.
		Connection dbconn = connect(args);

		// Prompt user for questions
		Scanner input = new Scanner(System.in);
		String buff = null;

		// Used for executing a query.
		Statement stmt = null;
		ResultSet answer = null;

		// Take in user input until 'exit' is entered.
		while (true) {
			// Print menu options.
			System.out.print(menu);
			// Get user input.
			buff = input.nextLine();

			// Query users question, or exit.
			if (buff.equals("1")) {
				// Query quesiton a.
				query1(dbconn, stmt, answer, input);
			} else if (buff.equals("2")) {
				// Query quesiton b.
				query2(dbconn, stmt, answer);
			} else if (buff.equals("3")) {
				// Query quesiton c.
				query3(dbconn, stmt, answer, input);
			} else if (buff.equals("4")) {
				// Query quesiton d.
				query4(dbconn, stmt, answer, input);
			} else if (buff.equals("5")) {
				// Insert appointment record.
				insertApp(dbconn, stmt, answer, input);
			} else if (buff.equals("6")) {
				// Delete appointment record.
				deleteApp(dbconn, stmt, answer, input);
			} else if (buff.equals("7")) {
				// Update appointment record.
				updateApp(dbconn, stmt, answer, input);
			} else if (buff.equals("8")) {
				// Insert user record.
				insertUser(dbconn, stmt, answer, input);
			} else if (buff.equals("9")) {
				// Delete user record.
				deleteUser(dbconn, stmt, answer, input);
			} else if (buff.equals("10")) {
				// Insert employee record.
				insertEmp(dbconn, stmt, answer, input);
			} else if (buff.equals("11")) {
				// Delete employee record.
				deleteEmp(dbconn, stmt, answer, input);
			} else if (buff.equals("12")) {
				// Update employee record.
				updateEmp(dbconn, stmt, answer, input);
			} else if (buff.equals("13")) {
				// Insert service record.
				insertSvc(dbconn, stmt, answer, input);
			} else if (buff.equals("14")) {
				// Delete service record.
				deleteSvc(dbconn, stmt, answer, input);
			} else if (buff.equals("15")) {
				// Update service record.
				updateSvc(dbconn, stmt, answer, input);
			} else if (buff.equals("exit")) {
				// Close and exit.
				input.close();
				break;
			}
		}

		// Shut down the connection to the DBMS.
		try {
			dbconn.close();
		} catch (SQLException e) {
			System.err.println("ERROR:\tCould not close connection to DBMS!");
			System.exit(-1);
		}
	}
}
