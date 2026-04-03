package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/expense_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "24becc16"; 

    public static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully");
            return conn;

        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL Driver not found!");
            e.printStackTrace();

        } catch (SQLException e) {
            System.out.println("Database Connection Failed!");
            e.printStackTrace();   

        } catch (Exception e) {
            System.out.println("Unexpected Error!");
            e.printStackTrace();
        }

        return null; 
    }
}