package dev.danvega.contentcalendar.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    public static Connection connection=null;
    // Database credentials
    /*private static String url = "jdbc:postgresql://localhost:5432/postgres"; // Replace with your database URL
    private static String user = "postgres"; // Replace with your database username
    private static String password = "password"; // Replace with your database password
    */
    public static Connection connect() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "password";
        try {
            // Establish connection to the PostgreSQL database
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");

        } catch (SQLException e) {
            System.out.println("Opps!! Cannot Connect to database !!");
            e.printStackTrace();
        }
        return connection;
        
    }

    public static void close(){
        if (connection != null) 
        {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
    }

    public static Connection getConnection()
    {
        return connection;
    }
}

