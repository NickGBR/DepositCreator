package ru.interns.deposit.service.enums;

public enum Errors {
    PERSONAL_DATA_ALREADY_EXIST(0),
    PASSPORT_ALREADY_EXIST(4),
    INVALID_USER_NAME(2),
    INVALID_PASSWORD(3),
    USER_LOGIN_ALREADY_EXIST(5);

    private int status;

    Errors(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
