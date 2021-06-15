package cmir2469.backend.domain;


import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Friend")
public class Friend implements Serializable {


    private Integer userID;
    private Integer userFriendID;

    @Column(name = "FriendStatusID")
    private Integer friendStatusID;

    @Id
    @Column(name = "ID")
    private String ID;


    @ManyToOne
    @JoinColumn(name="UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name="UserFriendID")
    private User friendOf;


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Friend(Integer userID, Integer userFriendID, Integer friendStatusID, String  ID) {
        this.userID = userID;
        this.userFriendID = userFriendID;
        this.friendStatusID = friendStatusID;
        this.ID = ID;

    }

    public Friend() {
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getUserFriendID() {
        return userFriendID;
    }

    public void setUserFriendID(Integer userFriendID) {
        this.userFriendID = userFriendID;
    }

    public Integer getFriendStatusID() {
        return friendStatusID;
    }

    public void setFriendStatusID(Integer friendStatusID) {
        this.friendStatusID = friendStatusID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriendOf() {
        return friendOf;
    }

    public void setFriendOf(User friendOf) {
        this.friendOf = friendOf;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "userID=" + userID +
                ", userFriendID=" + userFriendID +
                ", friendStatusID=" + friendStatusID +
                ", ID=" + ID +
                '}';
    }
}
