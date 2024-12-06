package dev.danvega.contentcalendar.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

@Repository
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
            String sql="SELECT description FROM Content";
            testBySql(sql);

        } catch (SQLException e) {
            System.out.println("Opps!! Cannot Connect to database !!");
            e.printStackTrace();
        }
        return connection;

    }


    public static void testBySql(String sql) {

        //String sql = "SELECT type FROM Content"; // Replace this with your actual SQL query
        if (connection == null) {
            System.out.println("Error: Connection is null. Cannot execute the query.");
            return;
        }
        try {
            // Create a statement object to execute the query
            Statement statement = connection.createStatement();

            // Execute the query and retrieve the result set
            ResultSet resultSet = statement.executeQuery(sql);
            // Check if there is at least one row returned
            if (resultSet.next()) { // Move to the first row
                // Get the value of the 'type' column from the first row
                String typeValue = resultSet.getString("type"); // Assuming 'type' is a string column
                // Print the retrieved value
                System.out.println("Type: " + typeValue);
            } else {
                System.out.println("No records found.");
            }

        } catch (SQLException e) {
            System.out.println("Test failed :"+ e.getMessage());

        }
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


