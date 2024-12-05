package dev.danvega.contentcalendar;

import dev.danvega.contentcalendar.config.ContentCalendarProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import dev.danvega.contentcalendar.repository.DBConnect;
@SpringBootApplication
@EnableConfigurationProperties(ContentCalendarProperties.class)
public class Application {

	public static void main(String[] args) {
		DBConnect.connect();
		SpringApplication.run(Application.class, args);
	}

}
