package dev.danvega.contentcalendar.model;

/*
 * @Id: An annotation from Spring Data that specifies
 * the primary key of the entity.
  @Column: An annotation that specifies the database column name
for the field it annotates.
 */
public class Content {
        private int id;                     // The unique identifier for the content
        private String title;               // The title of the content
        private String description;         // A description of the content
        private String status;              // The status of the content (e.g., ACTIVE, INACTIVE)
        private String contentType;         // The type of content (e.g., ARTICLE, VIDEO)
        private String url;                 // The URL associated with the content

        // Default constructor
        public Content() {}

        // Parameterized constructor
        public Content(int id, String title, String description, String status, String contentType, String url) {
                this.id = id;
                this.title = title;
                this.description = description;
                this.status = status;
                this.contentType = contentType;
                this.url = url;
        }



        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        public String getContentType() { return contentType; }
        public void setContentType(String contentType) { this.contentType = contentType; }
        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        @Override
        public String toString() {
                return "Content{" +
                        "id=" + id +
                        ", title='" + title + '\'' +
                        ", description='" + description + '\'' +
                        ", status='" + status + '\'' +
                        ", contentType='" + contentType + '\'' +
                        ", url='" + url + '\'' +
                        '}';
        }
}

