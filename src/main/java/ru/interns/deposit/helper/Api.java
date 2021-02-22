package ru.interns.deposit.helper;

import lombok.Getter;

@Getter
public enum Api {
    REGISTRATION_POST_REQUEST("/api/auth/user/registration"),
    LOGIN_PAGE("/api/view/login"),
    REGISTRATION_PAGE("/api/view/registration");

    private final String url;

    Api(String url) {
        this.url = url;
    }
}
