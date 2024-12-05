package dev.danvega.contentcalendar.repository;

import java.util.List;
import java.util.Optional;
import dev.danvega.contentcalendar.model.Content;
import org.springframework.stereotype.Repository;
/*
 * This interface declares that ContentRepository extends ListCrudRepository, which provides methods for 
 * performing CRUD operations on Content objects identified by an Integer type ID.
    By extending ListCrudRepository, ContentRepository inherits several useful methods, 
    such as:
    save(S entity): To save an entity.
    findById(ID id): To find an entity by its ID.
    deleteById(ID id): To delete an entity by its ID.
    findAll(): To retrieve all entities.
    And various other methods for manipulating the data.
    Custom Query Method
 * 
 */

import java.sql.*;
import java.util.ArrayList;

@Repository
public class ContentRepository {
    private static Connection connection=null;
    // Establish a connection to the database
    private void getConnection() throws SQLException {
        connection= DBConnect.connect();
    }

    public void save(Content content) {
        String sql = "INSERT INTO Content (title, desc, status, content_type, date_created, url) VALUES (?, ?, ?, ?, NOW(), ?)";

        // Use try-with-resources to ensure the PreparedStatement is closed properly
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, content.getTitle());
            statement.setString(2, content.getDescription()); // Note: consider changing "desc" to a different column name to avoid SQL reserved keyword issues
            statement.setString(3, content.getStatus());
            statement.setString(4, content.getContentType());
            statement.setString(5, content.getUrl());

            // Execute the insert operation
            statement.executeUpdate(); // This will save the content

        } catch (SQLException e) {
            System.out.println("Save content failed: " + e.getMessage()); // Improved error message
            e.printStackTrace(); // Print the stack trace for debugging purposes
        }
    }

    // Find all content records by content type
    public List<Content> findAllByContentType(String type) {
        List<Content> contents = new ArrayList<>();
        String sql = "SELECT * FROM Content WHERE content_type = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
                    contents.add(content); // Add to the list of contents
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }

        return contents; // Return the list of contents matching the specified content type
    }


    // Find content by ID
    public Optional<Content> findById(int id) {
        String sql = "SELECT * FROM Content WHERE id=?";
        Content content = null;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
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
            e.printStackTrace();
        }

        return Optional.ofNullable(content);
    }
    // Retrieve all content records
    public List<Content> getAllContent() {
        List<Content> contents = new ArrayList<>();
        String sql = "SELECT * FROM Content";

        try (
             PreparedStatement statement = connection.prepareStatement(sql);
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

    // Insert a new content record
    public void createContent(Content content) {
        String sql = "INSERT INTO Content (title, desc, status, content_type, date_created, url) VALUES (?, ?, ?, ?, NOW(), ?)";

        try (
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, content.getTitle());
            statement.setString(2, content.getDescription());
            statement.setString(3, content.getStatus());
            statement.setString(4, content.getContentType());
            statement.setString(5, content.getUrl());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    // Update an existing content record
    public void updateContent(Content content) {
        String sql = "UPDATE Content SET title=?, desc=?, status=?, content_type=?, url=? WHERE id=?";

        try (
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, content.getTitle());
            statement.setString(2, content.getDescription());
            statement.setString(3, content.getStatus());
            statement.setString(4, content.getContentType());
            statement.setString(5, content.getUrl());
            statement.setInt(6, content.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    // Delete a content record
    public void deleteContent(int id) {
        String sql = "DELETE FROM Content WHERE id=?";

        try (
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

    // Optionally, methods to find content by specific criteria can also be added here

    // Check if any records exist
    // Check if any records exist
    public int count() {
        String sql = "SELECT COUNT(*) FROM Content";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if count could not be determined
    }

    // Save all content records
    // Save all content records
    public void saveAll(List<Content> contents) {
        String sql = "INSERT INTO Content (title, desc, status, content_type, date_created, url) VALUES (?, ?, ?, ?, NOW(), ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (Content content : contents) {
                statement.setString(1, content.getTitle());
                statement.setString(2, content.getDescription());
                statement.setString(3, content.getStatus());
                statement.setString(4, content.getContentType());
                statement.setString(5, content.getUrl());
                statement.addBatch(); // Add to batch for later execution
            }
            statement.executeBatch(); // Execute all batch inserts

        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        }
    }

}