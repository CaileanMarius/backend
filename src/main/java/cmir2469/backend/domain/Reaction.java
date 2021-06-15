package cmir2469.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Reaction")
public class Reaction  implements Serializable {



    @Column(name = "Status")
    private String status;

    @Id
    @Column(name = "ID")
    private String ID;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "PostID")
    Post r_post;

    @ManyToOne
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "UserID")
    User r_user;

    public Reaction() {
    }

    public Reaction(String status, String ID, Post post, User user) {
        this.status = status;
        this.ID = ID;
        this.r_post = post;
        this.r_user = user;
    }

    public Post getPost() {
        return r_post;
    }

    public void setPost(Post post) {
        this.r_post = post;
    }

    public User getUser() {
        return r_user;
    }

    public void setUser(User user) {
        this.r_user = user;
    }

    public Integer getPostID() {
        return r_post.getID();
    }

    public void setPostID(Integer postID) {
        this.r_post.setID(postID);
    }

    public Integer getUserID() {
        return r_user.getID();
    }

    public void setUserID(Integer userID) {
        this.r_user.setID(userID);
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "Reaction{" +
                "status='" + status + '\'' +
                ", ID='" + ID + '\'' +
                //", post=" + r_post.getID().toString() +
                //", user=" + r_user.getID().toString() +
                '}';
    }
}
