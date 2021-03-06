package ru.alex.mini_bank.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "t_user", uniqueConstraints = @UniqueConstraint(columnNames = {"token"}))
public class User implements UserDetails {

    @Transient
    private static final GrantedAuthority role = () -> "User";

    @Id
    private String uniqueName;
    private String password;
    private String token;
    private boolean isSignedOut = true;
    private long balance = 0;

    public User() {
        //Nothing
    }

    public User(String uniqueName, String password) {
        this.uniqueName = uniqueName;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return uniqueName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSignedOut() {
        return isSignedOut;
    }

    public void setSignedOut(boolean signedOut) {
        isSignedOut = signedOut;
    }
}
