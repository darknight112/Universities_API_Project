import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;




public class Main {	
	
    
    public static void main(String[] args) {
    	Scanner sc = new Scanner(System.in);
    	System.out.println("API Manager");
    	boolean a = true;
    	while(a) {
    		System.out.println("Enter user");
        	String u = sc.next();
        	System.out.println("Enter password");
        	String p = sc.next();
        	if(u!="sa" || p!="root") {
        		System.out.println("database credintial invalid try again..");
        	}
        	else {
        		a=false;
        		System.out.println("Login Successfully");
        	}
    		
    	}
    	System.out.println("API Manager");
        System.out.println("1. Initialize Database");
        System.out.println("2. Backup Database");
        System.out.println("3. Delete Tables");
        System.out.println("4. Fetch Data From API");
        System.out.println("5. Fetch Data From Database");
        System.out.println("6. Dump Data To File");
        System.out.println("7. Print Universities By Country");
        System.out.println("8. Quit");
    	
    	String input= sc.next();
    	switch(input) {
    	default:
    		System.out.println("invalid input try again");
    		
    	case "1":
    		
    		break;
    	
    	
    	
    	
    	}
    	

    }
    
    
    public void createDb() {
    	Scanner sc = new Scanner(System.in);
    	dbInsert db = new dbInsert();
    	System.out.println("Enter the name of the database you want to create");
    	String dbName=sc.next();
    	db.createDb(dbName);
    	System.out.println("Enter the name of the country you want to search");
    	String country=sc.next();
    	String apiUrl = "http://universities.hipolabs.com/search?country="+country;

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
                //MyObject[] universities = gson.fromJson(response, MyObject[].class);
                MyObject[] universities = gson.fromJson(json.toString(), MyObject[].class);

                for(MyObject i: universities) {
                	System.out.println(i.name);
                }

                System.out.println("Data fetched from API and stored in database successfully.");
            } else {
                System.out.println("Error fetching data from API. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}