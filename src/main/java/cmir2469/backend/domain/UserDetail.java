package cmir2469.backend.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="UserDetail")
public class UserDetail implements Serializable {


    @Id
    @Column(name="UserID")
    private Integer ID;

    @Column(name="FirstName")
    private String firstName;

    @Column(name="LastName")
    private String lastName;

    @Column(name="Photo")
    private String photo;

    @Column(name="Birthday")
    private LocalDate birthday;

    @Column(name="Description")
    private String description;


    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "UserID")
    private User account;


    public UserDetail(Integer ID, String firstName, String lastName, String photo, LocalDate birthday, String description) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.birthday = birthday;
        this.description = description;
    }

    public UserDetail() {
    }


    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAccount() {
        return account;
    }

    public void setAccount(User account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "UserDetail{" +
                "ID=" + ID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photo='" + photo + '\'' +
                ", birthday=" + birthday +
                ", description='" + description + '\'' +
                ", account=" + account +
                '}';
    }
}
