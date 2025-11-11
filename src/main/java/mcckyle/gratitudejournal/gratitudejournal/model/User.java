//***************************************************************************************
//
//     Filename: User.java
//     Author: Kyle McColgan
//     Date: 04 December 2024
//     Description: This file stores information related to the user.
//
//***************************************************************************************

package mcckyle.gratitudejournal.gratitudejournal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

//***************************************************************************************

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

    @ManyToMany(fetch = FetchType.EAGER) //EAGER or LAZY gives errors...
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore  // Avoid circular reference when serializing User
    private Set<Role> roles;

    // Default constructor
    public User()
    {
        this.roles = new HashSet<>();
    }

    //Constructor with username, email, pw in that order (see UserService.java)
    public User(String username, String email, String password)
    {
        this(username, email, password, new HashSet<>());
    }

    public User(String username, String email, String password, Set<Role> roles)
    {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles != null ? roles : new HashSet<>();
    }

    //Getters and setters...
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getId()
    {
        return id;
    }

    public Set<Role> getRoles()
    {
        return roles;
    }

    public void setRoles(Set<Role> roles)
    {
        this.roles = roles;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }
}

//***************************************************************************************
