package mainpackage.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
	
	//jdbc:postgresql://localhost:5432/postgres postgres postgres
	
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
	
	public static DatabaseHandler getInstance(String url, String username, String password) {
		if(dh == null)
			return new DatabaseHandler(url, username, password);
		return dh;
	}
	
	public static DatabaseHandler getInstance(String host, String port, String dbName, String type, String username, String password) {
		if(dh == null)
			return new DatabaseHandler(host, port, dbName, type, username, password);
		return dh;
	}
	
	private DatabaseHandler() {
		assert this.informationInitilized : "Initialize the database access information first.";
		try {
			this.connection = DriverManager.getConnection(connectionURL.toString(),this.username, this.password);
			System.out.println("Connected!");
		} catch (SQLException e) {
			this.informationInitilized = false;
			e.printStackTrace();
		}
	}
	
	private DatabaseHandler(String url, String username, String password) {
		initializeAccessInformationURLMode(url, username, password);
		try {
			this.connection = DriverManager.getConnection(connectionURL.toString(),this.username, this.password);
			System.out.println("Connected using URL!");
		} catch (SQLException e) {
			this.informationInitilized = false;
			e.printStackTrace();
		}
	}
	
	private DatabaseHandler(String host, String port, String dbName, String type, String username, String password) {
		initializeAccessInformationClassicMode(host, port, dbName, type, username, password);
		try {
			this.connection = DriverManager.getConnection(connectionURL.toString(),this.username, this.password);
			System.out.println("Connected using classic mode!");
		} catch (SQLException e) {
			this.informationInitilized = false;
			e.printStackTrace();
		}
	}
	
	public void initializeAccessInformationClassicMode(String host, String port, String dbName, String type, String username, String password) {
		this.connectionURL = new StringBuilder("jdbc:" + type + "://" + host + ":" + port + "/" + dbName);
		System.out.println(connectionURL.toString());
		this.username = username;
		this.password = password;
		this.informationInitilized = true;
	}
	
	public void initializeAccessInformationURLMode(String url, String username, String password) {
		this.connectionURL = new StringBuilder(url);
		this.username = username;
		this.password = password;
		this.informationInitilized = true;
	}
	
	public void createSQLScript() {
		//interactive way of inserting input by the user, or, raw sql script (toggle raw or interactive oprions)
	}
	
	public void executeSQLScript(String script) {}

}
