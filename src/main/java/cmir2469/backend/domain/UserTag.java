package cmir2469.backend.domain;

import java.io.Serializable;

public class UserTag implements Serializable {

    private Integer userID;
    private Integer tagID;
    private String ID;

    public UserTag(Integer userID, Integer tagID, String ID) {
        this.userID = userID;
        this.tagID = tagID;
        this.ID = ID;
    }

    public UserTag() {
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "UserTag{" +
                "userID=" + userID +
                ", tagID=" + tagID +
                ", ID='" + ID + '\'' +
                '}';
    }
}
