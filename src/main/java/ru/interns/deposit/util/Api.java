package ru.interns.deposit.util;

import lombok.Getter;

@Getter
public enum Api {
    REGISTRATION_POST_REQUEST("/api/v1/auth/registration"),
    LOGIN_POST_REQUEST("/api/v1/auth/login"),


    LOGIN_PAGE("/api/v1/view/login"),
    REGISTRATION_PAGE("/api/v1/view/registration"),
    MAIN_PAGE("/api/v1/view/main_page");


    private final String url;

    Api(String url) {
        this.url = url;
    }
}
