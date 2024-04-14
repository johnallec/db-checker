package mainpackage.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
	
	private Connection connection;
	private StringBuilder connectionURL;
	private String username;
	private String password;
	
	private static DatabaseHandler dh;
	
	public static DatabaseHandler getInstance() {
		if(dh == null)
			return new DatabaseHandler();
		return dh;
	}
	
	private DatabaseHandler() {
		initializeAccessInformation();
		try {
			this.connection = DriverManager.getConnection(connectionURL.toString(),this.username, this.password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initializeAccessInformation() {
		this.connectionURL = new StringBuilder("jdbc:");
		//check the type of the db 'postgres' 'oracle' etc.. and append the type
		//jdbc:postgresql://localhost:5432/postgres
		//set username and password
	}
	
	public void createSQLScript() {
		//interactive way of inserting input by the user, or, raw sql script (toggle raw or interactive oprions)
	}
	
	public void executeSQLScript(String script) {}

}
