import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


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
	
	
	public void print() {
		try {

			Statement statement = conn.createStatement();

			String sql = "SELECT * FROM universities order by Country";
			ResultSet resultSet = statement.executeQuery(sql);
		
			while (resultSet.next()) {
				String name = resultSet.getString("Name");
				String Country = resultSet.getString("Country");
				String StateProvince = resultSet.getString("StateProvince");
				String AlphaTwoCode = resultSet.getString("AlphaTwoCode");
				String domain = resultSet.getString("Domains");
				String webPage = resultSet.getString("WebPages");
				
				System.out.println("----------------------------------------------------------");
			

				 System.out.println(name + "\t"  + " :: "+ Country + "\t"  + " :: "+ StateProvince + "\t"  + " :: "+ AlphaTwoCode + "\t"  +"\t" + " :: "+ domain + "\t"  + " :: "+ webPage);
			}

			resultSet.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void searchByCountry(String country) {
		try {

			Statement statement = conn.createStatement();

			String sql = "SELECT * FROM universities WHERE country = '" + country + "'";
			ResultSet resultSet = statement.executeQuery(sql);
			System.out.println("                Universities in " + country + ":");
			System.out.println("*******************************************************************");
			while (resultSet.next()) {
				//int id = resultSet.getInt("id");
				String name = resultSet.getString("Name");
				String StateProvince = resultSet.getString("StateProvince");
				String AlphaTwoCode = resultSet.getString("AlphaTwoCode");
				String domain = resultSet.getString("Domains");
				String webPage = resultSet.getString("WebPages");
				
				System.out.println("----------------------------------------------------------");
			

				 System.out.println(name + "\t" + " :: "+ StateProvince + "\t"  + " :: "+ AlphaTwoCode + "\t"  +"\t" + " :: "+ domain + "\t"  + " :: "+ webPage);
			}

			resultSet.close();
			statement.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	public void createDb(String dbName) {
//		DatabaseManager();
	    try {
	        // create database if it does not already exist
	        conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.databases WHERE name ="+ dbName+") CREATE DATABASE"+dbName);
	        conn.createStatement().execute("USE " + dbName);
	        // create tables if they do not already exist
	        conn.createStatement().execute("IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'Universities') CREATE TABLE Universities (id INTEGER IDENTITY PRIMARY KEY, Name VARCHAR(255), Country VARCHAR(255), StateProvince VARCHAR(255), AlphaTwoCode VARCHAR(2), Domains VARCHAR(255), WebPages VARCHAR(255))");

	        
	        
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public static void getData() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter the name of the country you want to search");
		String country = sc.next();
		String apiUrl = "http://universities.hipolabs.com/search?country=" + country;

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream inputStream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
				StringBuilder json = new StringBuilder();
				String line;

				while ((line = reader.readLine()) != null) {
					json.append(line);
				}

				reader.close();

				// Parse the JSON response using Gson
				Gson gson = new Gson();
				MyObject[] universities = gson.fromJson(json.toString(), MyObject[].class);
				//MyObject[] universities = gson.toJson(json, MyObject[].class);

	                // Insert the universities into the database
	                List<MyObject> universityList = Arrays.asList(universities);
	                MyObject[] universityArray = universityList.toArray(new MyObject[universityList.size()]);
	                insertUniversities(universityArray);

	                System.out.println("Data fetched from API and stored in database successfully.");
			} else {
				System.out.println("Error fetching data from API. Response code: " + responseCode);
			}

			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public List<MyObject> saveData() {
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
				universities.add(new MyObject(name,country,alphaTwoCode,stateProvince, Arrays.asList(webPages) ,
						Arrays.asList(domains)));
			}
		} catch (SQLException e) {
			System.out.println("Error retrieving universities from database.");
			e.printStackTrace();
		}
		return universities;
	}
	
	public void backup() {
		List<MyObject> universities = saveData();
		try (FileWriter fileWriter = new FileWriter("backup")) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			gson.toJson(universities, fileWriter);
			System.out.println("Data saved to file backup successfully.");
		} catch (IOException e) {
			System.out.println("Error saving data to file.");
			e.printStackTrace();
		}
		
		
	}


	public static void insertUniversities(MyObject[] universities) {
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
