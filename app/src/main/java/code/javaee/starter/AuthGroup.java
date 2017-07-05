package code.javaee.starter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class AuthGroup implements Serializable {


    @Id
    private String groupName;

    @ManyToMany
    @JoinTable(
        joinColumns = @JoinColumn(name = "groupName", referencedColumnName = "groupName"),
        inverseJoinColumns = @JoinColumn(name = "userName", referencedColumnName = "userName"))
    private List<User> users;


    protected AuthGroup() {
        super();
    }

    public AuthGroup(String groupName, List<User> users) {
        this.groupName = groupName;
        this.users = users;
    }
}
