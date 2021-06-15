package cmir2469.backend.domain.dtos;

import java.util.List;

public class ListFriend {

    private String usernameFriend;
    private List<String> tags;

    public String getUsernameFriend() {
        return usernameFriend;
    }

    public void setUsernameFriend(String usernameFriend) {
        this.usernameFriend = usernameFriend;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "ListFriend{" +
                "usernameFriend='" + usernameFriend + '\'' +
                ", tags=" + tags +
                '}';
    }
}
