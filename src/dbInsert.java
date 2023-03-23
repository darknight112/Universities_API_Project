import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fetchData.University;

public class dbInsert {
	private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private static final String URL = "jdbc:sqlserver://localhost:1433;" + "databaseName=universities;"
			+ "encrypt=true;" + "trustServerCertificate=true";
	private  String USER = "sa";
	private  String PASSWORD = "root";

	private static Connection conn;

	public void DatabaseManager() {
		try {
			Class.forName(DRIVER_CLASS);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	

	public void createDb(String dbName) {
	    try {
	        // create database if it does not already exist
	        conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = universities) CREATE DATABASE"+dbName);
	        
	        // create tables if they do not already exist
	        conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Universities') CREATE TABLE Universities (id INTEGER IDENTITY PRIMARY KEY, Name VARCHAR(255), Country VARCHAR(255), StateProvince VARCHAR(255), AlphaTwoCode VARCHAR(2), Domains VARCHAR(255), WebPages VARCHAR(255))");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public List<MyObject> printData() {
		List<MyObject> universities = new ArrayList<>();
		try (Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM universities")) {
			while (resultSet.next()) {
				String name = resultSet.getString("name");
				String country = resultSet.getString("country");
				String alphaTwoCode = resultSet.getString("AlphaTwoCode");
				String stateProvince = resultSet.getString("StateProvince");
				String[] domains = resultSet.getString("domains").split(",");
				String[] webPages = resultSet.getString("webpages").split(",");
				universities.add(new MyObject(String name,String country1,String alpha_two_code1,String state1, String domains1[] ,
						String	web_pages1[]));
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving universities from database.");
			e.printStackTrace();
		}
		return universities;
	}
	
	public void backup() {
		
		
		
	}


	public void insertUniversities(MyObject[] universities) {
		// insert universities data into the database
		try {
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Universities VALUES (?, ?, ?, ?, ?, ?)");
			for (MyObject university : universities) {
				pstmt.setString(1, university.getName());
				pstmt.setString(2, university.getCountry());
				pstmt.setString(3, university.getState());
				pstmt.setString(4, university.getAlpha_two_code());
				pstmt.setString(5, String.join(",", university.getDomains()));
				pstmt.setString(6, String.join(",", university.getWeb_pages()));
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteTable() throws SQLException {
		String sql = "DROP TABLE IF EXISTS Universities";
		try (Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println("Universities table has been deleted.");
		}
	}
	
}
