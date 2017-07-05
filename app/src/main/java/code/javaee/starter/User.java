package code.javaee.starter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class User implements Serializable {

    @Id
    private String userName;

    private String password;

    @ManyToMany(mappedBy = "users")
    private List<AuthGroup> groups;

    protected User() {
        super();
    }

    public User(String userName, String password, List<AuthGroup> groups) {
        this.userName = userName;
        this.password = password;
        this.groups = groups;
    }
}
