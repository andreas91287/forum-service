package ait.cohort46.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserProfile extends User {
    private boolean passwordNotExpired;

    public UserProfile(String username, String password, Collection<? extends GrantedAuthority> authorities, boolean passwordNotExpired) {
        super(username, password, authorities);
        this.passwordNotExpired = passwordNotExpired;
    }

    public boolean isPasswordNotExpired() {
        return passwordNotExpired;
    }
}
