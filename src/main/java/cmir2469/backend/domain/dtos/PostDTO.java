package cmir2469.backend.domain.dtos;

import java.time.LocalDateTime;

public class PostDTO {

    private Integer idPost;
    private String usernameUser;
    private String tagName;
    private LocalDateTime createdDate;
    private String description;

    public PostDTO() {
    }

    public Integer getIdPost() {
        return idPost;
    }

    public void setIdPost(Integer idPost) {
        this.idPost = idPost;
    }

    public String getUsernameUser() {
        return usernameUser;
    }

    public void setUsernameUser(String usernameUser) {
        this.usernameUser = usernameUser;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PostDTO{" +
                "usernameUser='" + usernameUser + '\'' +
                ", tagName='" + tagName + '\'' +
                ", createdDate=" + createdDate +
                ", description='" + description + '\'' +
                '}';
    }
}
