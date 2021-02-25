package ru.interns.deposit.util;

import lombok.Getter;

@Getter
public enum Api {
    ADD_PERSONAL_DATA_POST_REQUEST("/api/v1/user/add_data"),
    GET_PERSONAL_DATA_GET_REQUEST("/api/v1/user/get_data"),
    REGISTRATION_POST_REQUEST("/api/v1/auth/registration"),
    LOGIN_POST_REQUEST("/api/v1/auth/login"),


    LOGIN_PAGE("/api/v1/view/login"),
    REGISTRATION_PAGE("/api/v1/view/registration"),
    MAIN_PAGE("/api/v1/view/main_page"),
    ADD_PERSONAL_DATA_PAGE("/api/v1/view/add_personal_data_page");

    private final String url;

    Api(String url) {
        this.url = url;
    }
}
