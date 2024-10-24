package ait.cohort46.accounting.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@EqualsAndHashCode(of = "login")
@Document(collection = "users")
public class UserAccount {
    @Id
    private String login;
    @Setter
    private String firstName;
    @Setter
    private String lastName;
    @Setter
    private String password;
    private Set<Role> roles;
    @Setter
    private LocalDate passwordExpDate;

    public UserAccount() {
        roles = new HashSet<>();
        roles.add(Role.USER);
    }

    public UserAccount(String login, String firstName, String lastName, String password) {
        this();
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public boolean addRole(String role) {
        return roles.add(Role.valueOf(role.toUpperCase()));
    }

    public boolean removeRole(String role) {
        return roles.remove(Role.valueOf(role.toUpperCase()));
    }
}
