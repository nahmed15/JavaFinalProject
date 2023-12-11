
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

public class Dao {
	// instance fields
	static Connection connect = null;
	Statement statement = null;

	private String userTable="nadia_jpapa_users";
	private String ticketTable="nadia_jpapa_tickets";
	// constructor
	public Dao() {
	  
	}

	public Connection getConnection() {
		// Setup the connection with the DB
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}

	// CRUD implementation

	public void createTables() {
		// variables for SQL Query table creations
		 String createTicketsTable = "CREATE TABLE "+ticketTable+" (tid INT PRIMARY KEY AUTO_INCREMENT,user VARCHAR(255) NOT NULL,ticket_desc VARCHAR(400),start_date DATE,end_date DATE);";
		 String createUsersTable = "CREATE TABLE "+userTable+"(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(30), admin int)";


		try {

			// execute queries to create tables

			statement = getConnection().createStatement();

			statement.executeUpdate(createTicketsTable);
			statement.executeUpdate(createUsersTable);
			System.out.println("Created tables in given database...");

			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		// add users to user table
		addUsers();
	}

	public void addUsers() {
		// add list of users from userlist.csv file to users table

		// variables for SQL Query inserts
		String sql;

		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // list to hold (rows & cols)

		// read data from file
		try {
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
			System.out.println("There was a problem loading the file");
		}

		try {

			// Setup the connection with the DB

			statement = getConnection().createStatement();

			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {

				sql = "insert into "+userTable+"(uname,upass,admin) " + "values('" + rowData.get(0) + "'," + " '"
						+ rowData.get(1) + "','" + rowData.get(2) + "');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");

			// close statement object
			statement.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int insertRecords(String user, String ticketDesc, Date startDate, Date endDate) {
	    int id = 0;
	    try {
	        statement = getConnection().createStatement();
	        String sql = "INSERT INTO "+ticketTable+" (user, ticket_desc, start_date, end_date) VALUES (?, ?, ?, ?)";
	        PreparedStatement preparedStatement = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	        preparedStatement.setString(1, user);
	        preparedStatement.setString(2, ticketDesc);
	        preparedStatement.setDate(3, startDate);
	        preparedStatement.setDate(4, endDate);
	        preparedStatement.executeUpdate();
	        ResultSet resultSet = preparedStatement.getGeneratedKeys();
	        if (resultSet.next()) 
	            id = resultSet.getInt(1);
	    } catch (SQLException e) {e.printStackTrace();}
	    return id;
	}

	public ResultSet readRecords() {

		ResultSet results = null;
		try {
			statement = connect.createStatement();
			results = statement.executeQuery("SELECT * FROM "+ticketTable);
			System.out.println(results);
			if(results==null)
				System.out.println("Result set is null");
			else
				System.out.println("Result set Not null");
			//connect.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return results;
	}
	// continue coding for updateRecords implementation
	public void updateRecords(int ticketId, String newTicketUser, String newTicketDesc, Date newStartDate, Date newEndDate) {
	    String sql = "UPDATE " + ticketTable + " SET user=?, ticket_desc=?, start_date=?, end_date=? WHERE tid=?";
	    
	    try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
	        preparedStatement.setString(1, newTicketUser);
	        preparedStatement.setString(2, newTicketDesc);
	        preparedStatement.setDate(3, newStartDate);
	        preparedStatement.setDate(4, newEndDate);
	        preparedStatement.setInt(5, ticketId);

	        int rowsAffected = preparedStatement.executeUpdate();

	        if (rowsAffected > 0) {
	            JOptionPane.showMessageDialog(null, "Update successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            JOptionPane.showMessageDialog(null, "Update failed. No records were modified.", "Failure", JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Update failed. An error occurred.", "Failure", JOptionPane.ERROR_MESSAGE);
	    }
	}
	// continue coding for deleteRecords implementation
	public void deleteRecord(int ticketId) {
	    String sql = "DELETE FROM " + ticketTable + " WHERE tid=?";
	    
	    try (PreparedStatement preparedStatement = getConnection().prepareStatement(sql)) {
	        preparedStatement.setInt(1, ticketId);
	        int rowsAffected = preparedStatement.executeUpdate();
	        if (rowsAffected > 0) 
	            JOptionPane.showMessageDialog(null, "Deletion successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
	        else 
	            JOptionPane.showMessageDialog(null, "Deletion failed. No records were deleted.", "Failure", JOptionPane.ERROR_MESSAGE);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Deletion failed. An error occurred.", "Failure", JOptionPane.ERROR_MESSAGE);
	    }
	}
}
