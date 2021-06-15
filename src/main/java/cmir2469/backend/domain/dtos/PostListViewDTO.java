package cmir2469.backend.domain.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class PostListViewDTO {

    private Integer idPost;
    private String usernameUser;
    private String tagName;
    private LocalDateTime createdDate;
    private String description;
    private Integer likes;
    private Integer dislikes;
    private List<CommentDTO> commentsList;

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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public List<CommentDTO> getCommentsList() {
        return commentsList;
    }

    public void setCommentDTOList(List<CommentDTO> commentsList) {
        this.commentsList = commentsList;
    }

    @Override
    public String toString() {
        return "PostListViewDTO{" +
                "usernameUser='" + usernameUser + '\'' +
                ", tagName='" + tagName + '\'' +
                ", createdDate=" + createdDate +
                ", description='" + description + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", commentsList=" + commentsList +
                '}';
    }
}
