package mainpackage.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DatabaseHandler {
	
	//jdbc:postgresql://localhost:5432/postgres postgres postgres
	
	private Connection connection;
	private StringBuilder connectionURL;
	
	//public static final int DB_TEST = true;//REMOVE IT
	
	private static DatabaseHandler dh;

	public static DatabaseHandler getInstance() {
		return dh;
	}
	
	public static DatabaseHandler getInstance(String url, String username, String password) throws SQLException {
		if(dh == null) {
			dh = new DatabaseHandler(url, username, password);
			if(dh.connection == null) dh = null;
		}
		return dh;
	}
	
	public static DatabaseHandler getInstance(String host, String port, String dbName, String type, String username, String password) throws SQLException {
		if(dh == null) {
			dh = new DatabaseHandler(host, port, dbName, type, username, password);
			if(dh.connection == null) dh = null;
		}
		return dh;
	}
	
	private DatabaseHandler(String url, String username, String password) throws SQLException {
		initializeAccessInformationURLMode(url, username, password);
		this.connection = DriverManager.getConnection(connectionURL.toString(),username, password);
		SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, "Search Page", 770, 500);
		System.out.println("Connected using URL!");
	}
	
	private DatabaseHandler(String host, String port, String dbName, String type, String username, String password) throws SQLException {
		initializeAccessInformationClassicMode(host, port, dbName, type, username, password);
		this.connection = DriverManager.getConnection(connectionURL.toString(), username, password);
		SceneHandler.getSH().switchScene(SceneHandler.SEARCH_PAGE_PATH, "Search Page", 770, 500);
		System.out.println("Connected using classic mode!");
	}
	
	private void initializeAccessInformationClassicMode(String host, String port, String dbName, String type, String username, String password) {
		this.connectionURL = new StringBuilder("jdbc:" + type + "://" + host + ":" + port + "/" + dbName);
	}
	
	private void initializeAccessInformationURLMode(String url, String username, String password) {
		this.connectionURL = new StringBuilder(url);
	}
	
	public ArrayList<String> compareItems(String location, String compareAttribute, String[] data){
		ArrayList<String> results = null;
		
		String[] values = data;
		
		for(int i=0; i < values.length; i++) {
			try {
				if(results == null) results = select(location,compareAttribute, values[i]);
				else results.addAll(select(location,compareAttribute, values[i]));
			} catch (SQLException e) {
				SceneHandler.getSH().showError(e.getMessage());
				break;
			}
		}
		
		return results;
	}
	
	private ArrayList<String> select(String location, String comparisonAttribute, String value) throws SQLException {
		
		ArrayList<String> results = null;
		
		StringBuilder query = new StringBuilder("select " + comparisonAttribute + " from " + location + " where " + comparisonAttribute + " = ? ;");

		PreparedStatement ps = connection.prepareStatement(query.toString());
		ps.setString(1, value);
		ResultSet rs = ps.executeQuery();
		results = new ArrayList<String>();
		
		while(rs.next()) results.add(rs.getString(1));

		return results;
	}
	
	public void disconnect() {
		if(this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				SceneHandler.getSH().showError("Unxpected error closing the connection (might it be null?): " + e.getMessage());
				e.printStackTrace();
			}
		}
		dh = null;
	}
	
	public void createSQLScript() {
		//interactive way of inserting input by the user, or, raw sql script (toggle raw or interactive oprions)
	}
	
	public void executeSQLScript(String script) {
		
	}

}
