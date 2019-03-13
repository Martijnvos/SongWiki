package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NamedQueries({
    @NamedQuery(name = "Account.getAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findOne", query = "select a from Account a where a.username = :username")
})
public class Account {

    @Id
    @Size(min = 2, max = 40, message = "Username must be between 2 and 40 characters")
    private String username;

    @Size(min = 8, message = "Password must have more than 8 characters")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 13, message = "Age should not be less than 13")
    @Max(value = 130, message = "Age should not be greater than 130")
    private int age;

    // Cascading with orphanRemoval makes sure the corresponding roles are deleted
    // when an account is deleted
    @OneToMany(
        mappedBy = "account",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private Collection<Role> roles = new ArrayList<>();

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
        role.setAccount(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.setAccount(null);
    }

    public Account() {
        Role defaultRole = new Role("user");
        defaultRole.setAccount(this);
        roles.add(defaultRole);
    }
}
