package ru.interns.deposit.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.interns.deposit.db.dao.User;
import ru.interns.deposit.security.enums.Status;

import java.util.*;

@Data
@Builder
public class SecurityUser implements UserDetails {

    private final String userName;
    private final String password;
    private final Set<SimpleGrantedAuthority> authorities;
    private final boolean isActive;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails fromUser(User authorizationData) {
        return SecurityUser.builder().userName(authorizationData.getLogin())
                .password(authorizationData.getPassword())
                .isActive(authorizationData.getStatus().equals(Status.ACTIVE))
                .authorities(authorizationData.getRole().getAuthorities())
                .build();
    }
}
