package dev.danvega.contentcalendar.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import dev.danvega.contentcalendar.model.Content;
import dev.danvega.contentcalendar.model.Status;
import dev.danvega.contentcalendar.model.Type;


public class ContentJdbcTemplateRepository {
    /*
     * JdbcTemplate: A Spring class that simplifies database operations using JDBC.
     * This instance is used for executing SQL queries.
     */
    //may not need anymore
    private Connection jdbcTemplate=DBConnect.connect();

    public ContentJdbcTemplateRepository() {
        //connectToDatabase();
        // Initialize DBConnect and get the connection
        // Connect to the database
        // Create a DataSource using the single connection from DBConnect
        //Connection connection = DBConnect.getConnection();
        // Use SingleConnectionDataSource for JdbcTemplate
        //this.jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
    }

    private void connectToDatabase() {
            // Get connection using DBConnect
            //this.jdbcTemplate = DBConnect.connect();

    }
    /*
     * This private static method maps a ResultSet row to a Content object.
        It extracts the data from the result set using appropriate getter methods and constructs a Content object.
        SQL exceptions may be thrown if issues occur while accessing the database.
     * 
     */
    private static Content mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Content(rs.getInt("id"),
                rs.getString("title"),
                rs.getString("desc"),
                Status.valueOf(rs.getString("status")).toString(),
                Type.valueOf(rs.getString("content_type")).toString(),
               // rs.getObject("date_created", LocalDateTime.class),
               // rs.getObject("date_updated",LocalDateTime.class),
                rs.getString("url"));
    }

    public List<Content> getAllContent() {
        //String sql = "SELECT * FROM Content";
        //List<Content> contents = jdbcTemplate.query(sql, ContentJdbcTemplateRepository::mapRow);
        //return contents;
        List<Content> contents = new ArrayList<>();
        String sql = "SELECT * FROM Content";

        try (// Use DBConnect to get a manual connection
             PreparedStatement statement = jdbcTemplate.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Content content = new Content();
                content.setId(resultSet.getInt("id"));
                content.setTitle(resultSet.getString("title"));
                content.setDescription(resultSet.getString("desc"));
                content.setStatus(resultSet.getString("status"));
                content.setContentType(resultSet.getString("content_type"));
                content.setUrl(resultSet.getString("url"));
                contents.add(content);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions properly in a production app
        }
        return contents;

    }
    /*
     * Executes a SQL query to retrieve all content records from the Content table.
     Uses jdbcTemplate.query with the mapRow method to convert each row in the 
     result set to a Content object.
     */
    public void createContent(String title, String desc, Status status, Type contentType, String url) {
        //String sql = "INSERT INTO Content (title, desc, status, content_type, date_created, URL) VALUES (?, ?, ?, ?, NOW(), ?)";
        //jdbcTemplate.update(sql, title, desc, status, contentType, URL);
        String sql = "INSERT INTO Content (title, description, status, content_type, date_created, url) VALUES (?, ?, ?, ?, NOW(), ?)";

        try (// Get connection from DBConnect
             PreparedStatement statement = jdbcTemplate.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, desc);
            statement.setString(3, status.name()); // Store the enum as a string
            statement.setString(4, contentType.name()); // Store the enum as a string
            statement.setString(5, url);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    public void updateContent(int id, String title, String desc, Status status, Type contentType, String url) {
        //String sql = "UPDATE Content SET title=?, desc=?, status=?, content_type=?, date_updated=NOW(), url=? WHERE id=?";
        //jdbcTemplate.update(sql, title, desc, status, contentType, URL, id);
        String sql = "UPDATE Content SET title=?, description=?, status=?, content_type=?, date_updated=NOW(), url=? WHERE id=?";

        try (// Get connection from DBConnect
             PreparedStatement statement = jdbcTemplate.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, desc);
            statement.setString(3, status.name()); // Store the enum as a string
            statement.setString(4, contentType.name()); // Store the enum as a string
            statement.setString(5, url);
            statement.setInt(6, id); // The ID to update
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    public void deleteContent(int id) {
        //String sql = "DELETE FROM Content WHERE id=?";
        //jdbcTemplate.update(sql, id);
        String sql = "DELETE FROM Content WHERE id=?";

        try ( // Get connection from DBConnect
             PreparedStatement statement = jdbcTemplate.prepareStatement(sql)) {

            statement.setInt(1, id); // Specify the ID for deletion
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    public Content getContent(int id) {
        //String sql = "SELECT * FROM Content WHERE id=?";
        //Content content = jdbcTemplate.queryForObject(sql, new Object[]{id}, ContentJdbcTemplateRepository::mapRow);
       // return content;
        String sql = "SELECT * FROM Content WHERE id=?";
        Content content = null;

        try (// Get connection from DBConnect
             PreparedStatement statement = jdbcTemplate.prepareStatement(sql)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    content = new Content();
                    content.setId(resultSet.getInt("id"));
                    content.setTitle(resultSet.getString("title"));
                    content.setDescription(resultSet.getString("desc"));
                    content.setStatus(resultSet.getString("status"));
                    content.setContentType(resultSet.getString("content_type"));
                    content.setUrl(resultSet.getString("url"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle any SQL exceptions
        }

        return content; // Return the fetched content or null if not found
    }

    // Method to find all content records by content type
    public List<Content> findAllByContentType(String type) {
        List<Content> contents = new ArrayList<>();
        String sql = "SELECT * FROM Content WHERE content_type = ?";

        try (// Get connection from DBConnect
             PreparedStatement statement = jdbcTemplate.prepareStatement(sql)) {

            statement.setString(1, type); // Set the content type parameter
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Content content = new Content();
                    content.setId(resultSet.getInt("id"));
                    content.setTitle(resultSet.getString("title"));
                    content.setDescription(resultSet.getString("desc"));
                    content.setStatus(resultSet.getString("status"));
                    content.setContentType(resultSet.getString("content_type"));
                    content.setUrl(resultSet.getString("url"));
                    contents.add(content); // Add the retrieved content to the list
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }

        return contents; // Return the list of content records of the specified type
    }
}
