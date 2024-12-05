package dev.danvega.contentcalendar.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Repository;

import dev.danvega.contentcalendar.model.Content;
import dev.danvega.contentcalendar.model.Status;
import dev.danvega.contentcalendar.model.Type;

@Repository
public class ContentJdbcTemplateRepository {
    /*
     * JdbcTemplate: A Spring class that simplifies database operations using JDBC.
     * This instance is used for executing SQL queries.
     */
    //may not need anymore
    private final JdbcTemplate jdbcTemplate;

    public ContentJdbcTemplateRepository() {
        // Initialize DBConnect and get the connection
        DBConnect.connect(); // Connect to the database
        // Create a DataSource using the single connection from DBConnect
        Connection connection = DBConnect.getConnection();
        // Use SingleConnectionDataSource for JdbcTemplate
        this.jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(connection, false));
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
        String sql = "SELECT * FROM Content";
        List<Content> contents = jdbcTemplate.query(sql, ContentJdbcTemplateRepository::mapRow);
        return contents;
    }
    /*
     * Executes a SQL query to retrieve all content records from the Content table.
     Uses jdbcTemplate.query with the mapRow method to convert each row in the 
     result set to a Content object.
     */
    public void createContent(String title, String desc, Status status, Type contentType, String URL) {
        String sql = "INSERT INTO Content (title, desc, status, content_type, date_created, URL) VALUES (?, ?, ?, ?, NOW(), ?)";
        jdbcTemplate.update(sql, title, desc, status, contentType, URL);
    }

    public void updateContent(int id, String title, String desc, Status status, Type contentType, String URL) {
        String sql = "UPDATE Content SET title=?, desc=?, status=?, content_type=?, date_updated=NOW(), url=? WHERE id=?";
        jdbcTemplate.update(sql, title, desc, status, contentType, URL, id);
    }

    public void deleteContent(int id) {
        String sql = "DELETE FROM Content WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public Content getContent(int id) {
        String sql = "SELECT * FROM Content WHERE id=?";
        Content content = jdbcTemplate.queryForObject(sql, new Object[]{id}, ContentJdbcTemplateRepository::mapRow);
        return content;
    }
}
