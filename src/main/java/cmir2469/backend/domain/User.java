package cmir2469.backend.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "User")

public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer ID;

    @Column(name = "Username")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "Email")
    private String email;

    @Column(name = "Followers")
    private Integer followers;

    @Column(name = "Following")
    private Integer following;

    @ManyToMany(fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "UserTag", joinColumns = {
            @JoinColumn(name = "UserID") },
            inverseJoinColumns = { @JoinColumn(name = "TagID") })
    private Set<Tag> tags = new HashSet<Tag>(0);


    @OneToMany(mappedBy = "user", cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Comment> comments = new HashSet<Comment>(0);

    @OneToMany(mappedBy = "user",  cascade = { CascadeType.ALL })
    @ToString.Exclude
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Post> posts = new HashSet<Post>(0);



    @OneToMany(mappedBy = "r_user", cascade = { CascadeType.ALL })
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonBackReference
    Set<Reaction> reactions;





    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)

    private Set<Friend> friends;

    @OneToMany(mappedBy="friendOf", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)

    private Set<Friend> friendsWith;


    @OneToOne(mappedBy="account", cascade = CascadeType.ALL)
    @Fetch(FetchMode.JOIN)
    private UserDetail userDetail;


    public User(Integer ID, String username, String password, String email, Integer followers, Integer following, Set<Tag> tags, Set<Comment> comments, Set<Post> posts, Set<Reaction> reactions, Set<Friend> friends,  Set<Friend> friendsWith, UserDetail userDetail) {
        this.ID = ID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.followers = followers;
        this.following = following;
        this.tags = tags;
        this.comments = comments;
        this.posts = posts;
        this.reactions = reactions;
        this.friends = friends;
        this.friendsWith = friendsWith;
        this.userDetail = userDetail;
    }

    public User() {
    }



    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }


    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }


    public Set<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
    }



    public Set<Friend> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friend> friends) {
        this.friends = friends;
    }

    public Set<Friend> getFriendsWith() {
        return friendsWith;
    }

    public void setFriendsWith(Set<Friend> friendsWith) {
        this.friendsWith = friendsWith;
    }


}
