package cmir2469.backend.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Post")
public class Post {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name = "TagID")
    private Integer tagID;


    @Column(name = "CreatedDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Column(name = "Photo")
    private String photo;

    @Column(name = "Description")
    private String description;

    @Column(name = "Likes")
    private Integer likes;

    @Column(name = "Dislikes")
    private Integer dislikes;


    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "UserID")
    @JsonIgnore
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post",  cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Comment> comments = new HashSet<Comment>(
            0);

    @OneToMany(mappedBy = "r_post", cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonBackReference
    Set<Reaction> reactions;

    public Post(Integer ID, Integer tagID, LocalDateTime createdDate, String photo, String description, Integer likes, Integer dislikes,  Set<Comment> comments, Set<Reaction> reactions) {
        this.ID = ID;
        this.tagID = tagID;
        this.createdDate = createdDate;
        this.photo = photo;
        this.description = description;
        this.likes = likes;
        this.dislikes = dislikes;
        //this.user = user;
        this.comments = comments;
        this.reactions = reactions;
    }

    public Post() {

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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }


    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
    }


    public Set<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
    }

    @Override
    public String toString() {
        return "Post{" +
                "ID=" + ID +
                ", tagID=" + tagID +
                ", createdDate=" + createdDate +
                ", photo='" + photo + '\'' +
                ", description='" + description + '\'' +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                //", user=" + user +
                //", comments=" + comments +
                //", reactions=" + reactions +
                '}';
    }
}
