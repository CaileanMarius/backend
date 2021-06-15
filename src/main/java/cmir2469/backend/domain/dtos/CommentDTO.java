package cmir2469.backend.domain.dtos;

import java.time.LocalDateTime;

public class CommentDTO {

    private Integer idComment;
    private String usernameUser;
    private Integer postID;
    private LocalDateTime createdDate;
    private String description;

    public Integer getIdComment() {
        return idComment;
    }

    public void setIdComment(Integer idComment) {
        this.idComment = idComment;
    }

    public String getUsernameUser() {
        return usernameUser;
    }

    public void setUsernameUser(String usernameUser) {
        this.usernameUser = usernameUser;
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
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
        return "CommentDTO{" +
                "usernameUser='" + usernameUser + '\'' +
                ", postID='" + postID + '\'' +
                ", createdDate=" + createdDate +
                ", description='" + description + '\'' +
                '}';
    }
}
