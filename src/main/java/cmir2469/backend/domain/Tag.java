package cmir2469.backend.domain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="Tag")
public class Tag {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(name="name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @LazyCollection(LazyCollectionOption.FALSE)

    private Set<User> user = new HashSet<>();

    public Tag(Integer ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Tag() {
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    @Override
    public String toString() {
        return "Tag{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                '}';
    }
}
