package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Entity
@NamedQueries({
    @NamedQuery(name = "Account.getAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findOne", query = "select a from Account a where a.username = :username")
})
public class Account {

    @Id
    @GeneratedValue
    private int id;

    @Size(min = 2, max = 40, message = "Username must be between 2 and 40 characters")
    private String username;

    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 13, message = "Age should not be less than 13")
    @Max(value = 130, message = "Age should not be greater than 130")
    private int age;

    public int getId() { return id; }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
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
}
