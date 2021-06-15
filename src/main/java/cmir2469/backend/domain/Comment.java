package cmir2469.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "Comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer ID;

    @Column(name = "Description")
    private String description;

    @Column(name = "CreatedDate")
    private LocalDateTime createdDate;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "UserID")
    @JsonIgnore
    private User user;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "PostID")
    @JsonIgnore
    private Post post;



    public Comment(Integer ID, User user, Post post, String description, LocalDateTime createdDate) {
        this.ID = ID;
        this.user = user;
        this.post = post;
        this.description = description;
        this.createdDate = createdDate;
    }

    public Comment() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public Integer getUserID() {
        return user.getID();
    }

    public void setUserID(Integer userID) {
        this.user.setID(userID);
    }

    public Integer getPostID() {
        return post.getID();
    }

    public void setPostID(Integer postID) {
        this.post.setID(postID);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "ID=" + ID +
                ", description='" + description + '\'' +
                ", createdDate=" + createdDate +
               // ", user=" + user +
                //", post=" + post +
                '}';
    }
}
