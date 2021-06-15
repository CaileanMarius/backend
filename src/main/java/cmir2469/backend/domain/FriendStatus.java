package cmir2469.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "FriendStatus")
public class FriendStatus implements Serializable {

    @Id
    @Column(name = "ID")
    private Integer ID;

    @Column(name = "Status")
    private String status;





    public FriendStatus(Integer ID, String status) {
        this.ID = ID;
        this.status = status;
    }

    public FriendStatus() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendStatus{" +
                "ID=" + ID +
                ", status='" + status + '\'' +
                '}';
    }
}
