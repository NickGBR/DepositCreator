package ru.interns.deposit.util;
import lombok.Getter;

    @Getter
    public enum ClientErrorCode {
        USER_ALREADY_EXISTS(2),
        INVALID_LOGIN_PASSWORD(4);


        private final int value;

        ClientErrorCode(int value) {
            this.value = value;
        }
    }

