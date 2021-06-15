package cmir2469.backend.domain;

import java.io.Serializable;

public class PostTag implements Serializable {

    private Integer postID;
    private Integer tagID;


    public PostTag(Integer postID, Integer tagID, Integer userID) {
        this.postID = postID;
        this.tagID = tagID;
    }

    public PostTag() {
    }

    public Integer getPostID() {
        return postID;
    }

    public void setPostID(Integer postID) {
        this.postID = postID;
    }

    public Integer getTagID() {
        return tagID;
    }

    public void setTagID(Integer tagID) {
        this.tagID = tagID;
    }



    @Override
    public String toString() {
        return "PostTag{" +
                "postID=" + postID +
                ", tagID=" + tagID +
                '}';
    }
}
