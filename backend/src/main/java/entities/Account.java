package entities;

import websockets.listeners.AccountChangeListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Entity
@EntityListeners(AccountChangeListener.class)
@NamedQueries({
    @NamedQuery(name = "Account.getAll", query = "SELECT a FROM Account a LEFT JOIN FETCH a.playlists"),
    @NamedQuery(name = "Account.findOne", query = "select a from Account a LEFT JOIN FETCH a.playlists where a.username = :username")
})
public class Account {

    @Id
    @Size(min = 2, max = 40, message = "Username must be between 2 and 40 characters")
    private String username;

    @Size(min = 8, message = "Password must have more than 8 characters")
    private String password;

    @Email(message = "Email should be valid")
    private String email;

    @Min(value = 13, message = "You must be older than 13 in order to use SongWiki")
    @Max(value = 130, message = "Did you find the potion of eternal life? Age can't be higher than 130")
    private int age;

    @Enumerated(EnumType.STRING)
    private Role role = Role.user;

    @OneToMany(
        mappedBy = "account",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.EAGER
    )
    private Collection<Playlist> playlists = new HashSet<>();

    @Transient
    private List<Link> links = new ArrayList<>();

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

    public Role getRole() {
        return this.role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public Collection<Playlist> getPlaylists() {
        return playlists;
    }
    public void setPlaylists(Collection<Playlist> playlists) {
        playlists.forEach(this::addPlaylist);
    }

    public List<Link> getLinks() {
        return links;
    }

    public void addLink(String link, String rel) {
        Link newLink = new Link(link, rel);
        links.add(newLink);
    }

    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        playlist.setAccount(this);
    }

    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
        playlist.setAccount(null);
    }
}
