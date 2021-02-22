package ru.interns.deposit.security.enums;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum Role {
    USER(Stream.of(Permission.CREATE_DEPOSIT).collect(Collectors.toSet()));

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    private final Set<Permission> permissions;

    /* SimpleGrantedAuthority - объект спринга, которые отвечает за првоерку прав доступа, данный метод переводит наши права, в права понятные
 спринг security*/
    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new org.springframework.security.core.authority.SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }

}
