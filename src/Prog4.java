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
 * 		interface of a DMV api.
 * 
 * @Requirements
 * 		- Basic understanding / access to the commandline.
 * 		- Java 16 or earlier.
 * 
 * @Usage
 * 		- Add the Oracle JDBC driver to your CLASSPATH environment variable:
 * 		export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 * 
 * 		- Compile the program:
 * 		$ javac Prog4.java
 * 
 * 		- Finally, Run the program; where <username> and <password> are the
 * 		users aloe user name and password respectivly:
 * 		$ java Prog4 <username> <password>
 * 
 * @Examples
 * 		$ export CLASSPATH=/usr/lib/oracle/19.8/client64/lib/ojdbc8.jar:${CLASSPATH}
 * 		$ javac Prog4.java
 * 		$ java Prog4 <username> <password>
 */
public class Prog4 {
	// Menu that gets printed to the user.
	private static final String menu =
		"\n[1] List users whose Id's will expire given a date(MM/DD/YYYY).\n" +
		"[2] For the previous month, count every type of appointment and check how many of them successfully got their IDs.\n" +
		"[3] Disply the collected fee amount for every department for a given month(MM/YYYY).\n" +
		"[4] Custom...\n" +
		"[5] Make an appointment.\n" +
		"[6] Cancel an appointment.\n" +
		"[7] Change an appointment service.\n" +
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
	private static final String q1 = "";
	private static final String q2 = "select service_id, count(success) from " +
									 "dryngler.Appointment where success = 1 group by service_id";
	private static final String q3 = "";
	private static final String q4 = "";

	/**
	 * Execute insert/update/delete.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param query - String reprsents query.
	 */
	private static void execute(Connection dbconn, Statement stmt, String query) {
		try {
			// Execute query.
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query);
			System.out.println("Your appointment has been made!");
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
	 * @param input - Scanner used to get date from the user.
	 */
	private static void query1(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get date.
		String date = null;
		while(true){
			System.out.print("Enter date(MM/DD/YYYY): ");
			date = input.nextLine();
			DateFormat sdf = new SimpleDateFormat("MM/DD/YYYY");
			sdf.setLenient(false);
			try {
				sdf.parse(date);
				System.out.println(sdf.toString());
			} catch (ParseException e) {
				System.err.println("Error:\tEnter a vaild date!");
				continue;
			}
			break;
		}
		// String query = String.format(q1, date);
		// try {
		// 	// Execute query.
		// 	stmt = dbconn.createStatement();
		// 	answer = stmt.executeQuery(query);
		// 	if(answer != null) {
		// 		// Output data about query.
		// 		answer.next();
		// 		System.out.println("Result: "+ answer.getString(1));
		// 	} else {
		// 		System.out.println("No result returned from query!");
		// 	}
		// 	stmt.close();
		// } catch (SQLException e) {
		// 	System.err.println("*** SQLException:  "
		// 	+ "Could not fetch query results.");
		// 	System.err.println("\tMessage:   " + e.getMessage());
		// 	System.err.println("\tSQLState:  " + e.getSQLState());
		// 	System.err.println("\tErrorCode: " + e.getErrorCode());
		// 	System.exit(-1);
		// }
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

				printResults(answer, {0,0});

				System.out.println("SERVICE_ID \t UNSUCCESSFUL APPOINTMENTS");
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
		// Get date.
		String date = null;
		while(true){
			System.out.print("Enter date(MM/YYYY): ");
			date = input.nextLine();
			DateFormat sdf = new SimpleDateFormat("MM/YYYY");
			sdf.setLenient(false);
			try {
				sdf.parse(date);
				System.out.println(sdf.toString());
			} catch (ParseException e) {
				System.err.println("Error:\tEnter a vaild date!");
				continue;
			}
			break;
		}

		// String query = String.format(q3, date);
		// try {
		// 	// Execute query.
		// 	stmt = dbconn.createStatement();
		// 	answer = stmt.executeQuery(query);
		// 	if(answer != null) {
		// 		while(answer.next()) {
		// 			System.out.println(answer.getString(1));
		// 		}
		// 	}
		// 	stmt.close();
		// } catch (SQLException e) {
		// 	System.err.println("*** SQLException:  "
		// 	+ "Could not fetch query results.");
		// 	System.err.println("\tMessage:   " + e.getMessage());
		// 	System.err.println("\tSQLState:  " + e.getSQLState());
		// 	System.err.println("\tErrorCode: " + e.getErrorCode());
		// 	System.exit(-1);
		// }
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
	 * Execute appointment insert from user.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void insertApp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user id.
		String id = null;
		while(true){
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
		String insertApp = "";
		String query = String.format(insertApp, id);
		try {
			// Execute query.
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query);
			System.out.println("Your appointment has been made!");
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
	 * Execute appointment delete from user.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void deleteApp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user id.
		String id = null;
		while(true){
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
		// String query = String.format(insertApp, id, service);
		// try {
		// 	// Execute query.
		// 	stmt = dbconn.createStatement();
		// 	stmt.executeUpdate(query);
		// 	System.out.println("Your appointment has been made!");
		// 	stmt.close();
		// } catch (SQLException e) {
		// 	System.err.println("*** SQLException:  "
		// 	+ "Could not fetch query results.");
		// 	System.err.println("\tMessage:   " + e.getMessage());
		// 	System.err.println("\tSQLState:  " + e.getSQLState());
		// 	System.err.println("\tErrorCode: " + e.getErrorCode());
		// 	System.exit(-1);
		// }
	}

	/**
	 * Execute appointment update from user.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void updateApp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user id.
		String id = null;
		while(true){
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
		// String query = String.format(insertApp, id, service);
		// try {
		// 	// Execute query.
		// 	stmt = dbconn.createStatement();
		// 	stmt.executeUpdate(query);
		// 	System.out.println("Your appointment has been made!");
		// 	stmt.close();
		// } catch (SQLException e) {
		// 	System.err.println("*** SQLException:  "
		// 	+ "Could not fetch query results.");
		// 	System.err.println("\tMessage:   " + e.getMessage());
		// 	System.err.println("\tSQLState:  " + e.getSQLState());
		// 	System.err.println("\tErrorCode: " + e.getErrorCode());
		// 	System.exit(-1);
		// }
	}

	/**
	 * Execute User insert from user.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void insertUser(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get user first and last name.
		System.out.print("Enter first name: ");
		String firstName = input.nextLine();
		System.out.print("Enter last name: ");
		String lastName = input.nextLine();

		String query = "insert into customer values (customer_seq.nextval, " + firstName + ", "+ lastName +") ";
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute User delete from user.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
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
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void insertEmp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get job and dept id.
		System.out.print("Enter Job ID: ");
		String jobId = input.nextLine();
		System.out.print("Enter Department ID: ");
		String deptId = input.nextLine();

		String query = "insert into Employee values (employee_seq.nextval, 1, 1, "+jobId+", "+deptId+")";
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute Employee delete from user.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
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
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void updateEmp(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		
	}

	/**
	 * Execute Service insert from user.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void insertSvc(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		// Get Service name, fee and lifetime.
		System.out.print("Enter a Service name: ");
		String svcName = input.nextLine();
		System.out.print("Enter a Service fee: ");
		String fee = input.nextLine();
		System.out.print("Enter number of years vaild: ");
		String lifetime = input.nextLine();

		String query = "insert into service values (service_seq.nextval, "+svcName+", "+fee+", "+lifetime+")";
		execute(dbconn, stmt, query);
	}

	/**
	 * Execute Service delete from user.
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
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
	 * @param dbconn - Current connection with database.
	 * @param stmt - Object used to execture SQL.
	 * @param answer - Table of data representing the result of the query.
	 * @param input - Scanner used to get school name from the user.
	 */
	private static void updateSvc(Connection dbconn, Statement stmt, ResultSet answer, Scanner input) {
		
	}

	/*                                                                                                                                                                                                                                                                                                     
   * Purpose: Print the ResultSet from a query. The column names are printed first and then each tuple thereafter. 
   * The width of columns is found with getColumnDisplaySize(). 
   * Params: ResultSet answer: to be printed
   *         int[] dtypes: datatype of each column represented by bits to know whether to call getInt(), getString(), or getDouble()
   *                       value in array mean -> 0 : string; 1: int; -1 : double
   */
  void printResults(ResultSet answer, int[] dtypes){
    try {
      System.out.println();
      ResultSetMetaData answermetadata = answer.getMetaData();
      for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
          String name = answermetadata.getColumnName(i);
          if (dtypes[i] == 0) {
            int width = answermetadata.getColumnDisplaySize(i);
            System.out.print(name);
            for (int j = name.length(); j <= width; j++){
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
              if (dtypes[i] == 1) {
                int value = answer.getInt(i);
                tuple += value;
                for (int j = String.valueOf(value).length(); j <= answermetadata.getColumnName(i).length() + 4; j++) { // +4 for tab
                   tuple += " ";
                } 
              } else if (dtypes[i] == 0) {
                tuple += answer.getString(i);
                for (int j = tuple.length(); j <= width; j++){
                  tuple += " ";
                }
              } else {
                double dub = Math.round(answer.getDouble(i) * 100.00) / 100.00; //round to 2 decimal places
                tuple += dub;
                for (int j = String.valueOf(dub).length(); j <= answermetadata.getColumnName(i).length() + 5; j++) { // +5 for tab and 1 for decimal place in double
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
			if(buff.equals("1")){
				// Query quesiton a.
				query1(dbconn, stmt, answer, input);
			} else if(buff.equals("2")){
				// Query quesiton b.
				query2(dbconn, stmt, answer);
			} else if(buff.equals("3")){
				// Query quesiton c.
				query3(dbconn, stmt, answer, input);
			} else if(buff.equals("4")){
				// Query quesiton d.
				query4(dbconn, stmt, answer, input);
			} else if(buff.equals("5")){
				// Insert appointment record.
				insertApp(dbconn, stmt, answer, input);
			} else if(buff.equals("6")){
				// Delete appointment record.
				deleteApp(dbconn, stmt, answer, input);
			} else if(buff.equals("7")){
				// Update appointment record.
				updateApp(dbconn, stmt, answer, input);
			}  else if(buff.equals("8")){
				// Insert user record.
				insertUser(dbconn, stmt, answer, input);
			} else if(buff.equals("9")){
				// Delete user record.
				deleteUser(dbconn, stmt, answer, input);
			} else if(buff.equals("10")){
				// Insert employee record.
				insertEmp(dbconn, stmt, answer, input);
			} else if(buff.equals("11")){
				// Delete employee record.
				deleteEmp(dbconn, stmt, answer, input);
			} else if(buff.equals("12")){
				// Update employee record.
				updateEmp(dbconn, stmt, answer, input);
			}  else if(buff.equals("13")){
				// Insert service record.
				insertSvc(dbconn, stmt, answer, input);
			} else if(buff.equals("14")){
				// Delete service record.
				deleteSvc(dbconn, stmt, answer, input);
			} else if(buff.equals("15")){
				// Update service record.
				updateSvc(dbconn, stmt, answer, input);
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
