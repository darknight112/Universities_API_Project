import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		dbInsert db = new dbInsert();
		System.out.println("API Manager");
		boolean a = true;
		while (a) {
			System.out.println("Enter user");
//			String u1="sa";
//			String p1="root";

			String u = sc.next();
			System.out.println("Enter password");
			String p = sc.next();
			if (u.equals("sa") && p.equals("root")) {
				a = false;
				System.out.println("Login Successfully");
			} else {
				System.out.println("database credintial invalid try again..");
			}

		}

		boolean loop = true;
		while (loop) {
			System.out.println("API Manager");
			System.out.println("1. Create Database");
			System.out.println("2. Backup Database");
			System.out.println("3. Delete Tables");
			System.out.println("4. Print Data");
			System.out.println("5. Print Universities By Country");
			System.out.println("6. Quit");
			String input = sc.next();

			switch (input) {
			default:
				System.out.println("invalid input try again");

			case "1":
				System.out.println("Enter the name of the database you want to create");
				String dbName = sc.nextLine();
				db.createDb(dbName);
				break;
			case "2":
				db.backup();
				break;
			case "3":
				try {
					dbInsert.deleteTable();
				} catch (SQLException e) {
					System.out.println("Error deleting table from database.");
					e.printStackTrace();
				}
				break;
			case "4":
				db.print();
				break;
			case "5":
				System.out.println("Enter the name of the country");
				String country = sc.next();
				db.searchByCountry(country);
				break;
			case "6":
				loop = false;
				System.out.println("Exit..");

				break;
			}

		}

	}

}