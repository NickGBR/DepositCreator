package ru.interns.deposit.security.enums;

import lombok.Getter;

@Getter
public enum Permission {
    CREATE_DEPOSIT("deposit:create");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }
}
