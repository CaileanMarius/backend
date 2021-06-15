package cmir2469.backend.domain.dtos;

public class FriendDTO {

    private String usernameUserLogged;
    private String getUsernameUserToAdd;
    private Integer status;

    public FriendDTO() {
    }

    public String getUsernameUserLogged() {
        return usernameUserLogged;
    }

    public void setUsernameUserLogged(String usernameUserLogged) {
        this.usernameUserLogged = usernameUserLogged;
    }

    public String getGetUsernameUserToAdd() {
        return getUsernameUserToAdd;
    }

    public void setGetUsernameUserToAdd(String getUsernameUserToAdd) {
        this.getUsernameUserToAdd = getUsernameUserToAdd;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendDTO{" +
                "usernameUserLogged='" + usernameUserLogged + '\'' +
                ", getUsernameUserToAdd='" + getUsernameUserToAdd + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
