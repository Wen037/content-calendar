package dev.danvega.contentcalendar.config;

import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import dev.danvega.contentcalendar.repository.DBConnect;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.danvega.contentcalendar.model.Content;
import dev.danvega.contentcalendar.repository.ContentRepository;
/*
 *  DataLoader class is responsible for initializing the content database by 
 * loading data from a specified JSON file when the Spring Boot application 
 * is started, but only if the repository currently contains no data. 
 * This is a common pattern for seeding a database with initial data.
 */

import java.io.InputStream;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class DataLoader {

    private final ContentRepository repository; // Use ContentRepository to handle DB operations
    private final ObjectMapper objectMapper;

    // Constructor accepts an ObjectMapper and initializes the repository
    public DataLoader(ObjectMapper objectMapper) {
        this.repository = new ContentRepository(); // Initialize the ContentRepository
        this.objectMapper = objectMapper; // Use provided ObjectMapper for JSON processing
    }

    // Method to load data from JSON and populate the database
    public void loadData() throws Exception {
        if (repository.count() == 0) { // Check if the database is empty
            try (InputStream inputStream = TypeReference.class.getResourceAsStream("/data/content.json")) {
                List<Content> contents = objectMapper.readValue(inputStream, new TypeReference<List<Content>>() {});
                repository.saveAll(contents); // Save the contents to the database
            }
        }
    }
}



