package mainpackage.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
	
	private Connection connection;
	private StringBuilder connectionURL;
	private String username;
	private String password;
	private boolean informationInitilized = false;
	
	private static DatabaseHandler dh;
	
	public static DatabaseHandler getInstance() {
		if(dh == null)
			return new DatabaseHandler();
		return dh;
	}
	
	private DatabaseHandler() {
		assert this.informationInitilized : "Initialize the database access information first.";
		//initializeAccessInformation();
		try {
			this.connection = DriverManager.getConnection(connectionURL.toString(),this.username, this.password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void initializeAccessInformationClassicMode(String host, String port, String dbName, String type, String username, String password) {
		this.connectionURL = new StringBuilder("jdbc:" + type + "://" + host + ":" + port + "/" + dbName);
		System.out.println(connectionURL.toString());
		this.username = username;
		this.password = password;
		//jdbc:postgresql://localhost:5432/postgres
		//set username and password
		this.informationInitilized = true;
	}
	
	public void initializeAccessInformationURLMode(String url) {
		this.connectionURL = new StringBuilder(url);
		this.informationInitilized = true;
	}
	
	public void createSQLScript() {
		//interactive way of inserting input by the user, or, raw sql script (toggle raw or interactive oprions)
	}
	
	public void executeSQLScript(String script) {}

}
