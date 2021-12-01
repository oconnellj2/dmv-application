import java.sql.*;
import java.util.Scanner;

/**
 * Author: James O'Connell
 * Course: CSC 460, Fall 2021
 * Assignment: Program 4: Database Design and Implementation.
 * Instructor: L. McCann
 * Due Date: December 6th, 2021, at the beginning of class.
 * 
 * Purpose:
 * 		Using JDBC, this program offers the user a text-based client user
 * 		interface of a DMV api.
 * 
 * Requirements:
 * 		- Basic understanding / access to the commandline.
 * 		- Java 16 or earlier.
 * Usage:
 * 		- Add the Oracle JDBC driver to your CLASSPATH environment variable:
 * 		export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 * 
 * 		- Compile the program:
 * 		$ javac Prog4.java
 * 
 * 		- Finally, Run the program; where <username> and <password> are the users aloe
 * 		user name and password respectivly:
 * 		$ java Prog4 <username> <password>
 * 
 * Examples:
 * 		$ export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 * 		$ javac Prog4.java
 * 		$ java Prog4 <username> <password>
 */
public class Prog4 {
	// Menu that gets printed to the user.
	private static final String menu =
		"\n[a] List users whose Id's will expire given a date(MM/DD/YYYY).\n" +
		"[b] For the previous month, count every type of appointment and check how many of them successfully got their IDs.\n" +
		"[c] Disply the collected fee amount for every department for a given month(MM/YYYY).\n" +
		"[d] Custom...\n\n" +
		"Enter a-d to make a query! or...\n" +
		"Type insert, delete or update to modify data records! or...\n" +
		"Type exit.";
	// Queries.
	private static final String q1 = "";
	private static final String q2 = "";
	private static final String q3 = "";
	private static final String q4 = "";
	/**
	 * Connects the user to the DBMS.
	 * @param args - Command line arguments that hold the username nad password.
	 * @return The connection session with our database.
	 */
	private static Connection connect(String[] args) {
		// Handle command line args.
		String username = null;
		String password = null;
		if(args.length == 2) {
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
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 */
	private static void query1(Connection dbconn, Statement stmt, ResultSet answer) {
		try {
			// Execute query.
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(q1);
			if(answer != null) {
				// Output data about query.
				answer.next();
				System.out.println("Result: "+ answer.getString(1));
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
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 */
	private static void query2(Connection dbconn, Statement stmt, ResultSet answer) {
		try {
			// Execute query.
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(q2);
			if(answer != null) {
				// Output data about query.
				System.out.println();
				int count = 0;
				while(answer.next()){
					System.out.println(answer.getString(1)+" \t "+answer.getString(2));
					count++;
				}
				System.out.println("Total number of results: "+count);
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
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get year and district name from the user.
	 */
	private static void query3(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get year.
		String year = null;
		while(true){
			System.out.print("Enter year: ");
			year = input.next();
			if(year.equals("2017") || year.equals("2018") || year.equals("2019") || year.equals("2021")){
				break;
			} else {
				System.out.println("Enter a vaild year(2017, 2018, 2019 or 2021)!");
			}
		}
		// Get District.
		System.out.print("Enter a district name: ");
		String district = input.nextLine();
		district += input.nextLine().toUpperCase();
		// originally q3_helper instead of q3.
		String query = String.format(q3, year, district);
		try {
			// Execute query to see the number of schools.
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			if(answer != null) {
				int count = 0;
				while(answer.next()) {
					count = Integer.parseInt(answer.getString(1));
				}
				stmt.close();

				// Check if there are more than 10 schools.
				if(count >= 10) {
					// Execute query to get the schools.
					stmt = dbconn.createStatement();
					query = String.format(q3, year, district);
					answer = stmt.executeQuery(query);
					while(answer.next()) {
						System.out.println(answer.getString(1)+"\t"+answer.getString(2)+"\t"+answer.getString(3)+"\t"+answer.getString(4)+"\t"+answer.getString(5));
					}
				} else {
					// Not enough schools!
					System.out.println("I'm sorry, but district "+district+" has just "+count+" schools.");
				}
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
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void query4(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get School name.
		System.out.print("Enter a school name: ");
		String school = input.nextLine().toUpperCase();
		String query = String.format(q4, school, school, school, school);
		try {
			// Execute query to see the number of schools.
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			if(answer != null && answer.isBeforeFirst()) {
				// Execute query to get the schools.
				while(answer.next()) {
					System.out.println("Result: 2017: "+answer.getString(1)+
											 ", 2018: "+answer.getString(2)+
											 ", 2019: "+answer.getString(3)+
											 ", 2021: "+answer.getString(4));
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
		while(true) {
			// Print menu options.
			System.out.print(menu);
			// Get user input.
			buff = input.nextLine();

			// Query users question, or exit.
			if(buff.equals("a")){
				// Query quesiton a.
				query1(dbconn, stmt, answer);
			} else if(buff.equals("b")){
				// Query quesiton b.
				query2(dbconn, stmt, answer);
			} else if(buff.equals("c")){
				// Query quesiton c.
				query3(dbconn, stmt, answer, input);
			} else if(buff.equals("d")){
				// Query quesiton d.
				query4(dbconn, stmt, answer, input);
			} else if(buff.equals("insert")){
				// Insert record.
			} else if(buff.equals("delete")){
				// Insert record.
			} else if(buff.equals("update")){
				// Insert record.
			}else if(buff.equals("exit")){
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
