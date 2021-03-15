package ru.interns.deposit.service.enums;

public enum Errors {
    PERSONAL_DATA_ALREADY_EXIST(0),
    PASSPORT_ALREADY_EXIST(4),

    USER_NAME_IS_TOO_SHORT(10),
    USER_NAME_IS_TOO_LONG(11),
    USER_NAME_IS_INVALID(12),

    USER_SURNAME_IS_TOO_SHORT(13),
    USER_SURNAME_IS_TOO_LONG(14),
    USER_SURNAME_IS_INVALID(15),

    PASSPORT_NUMBER_LENGTH(16),
    PASSPORT_CONTAINS_WRONG_SYMBOLS(17),

    USER_AGE_IS_UNDER_14 (18),

    CHEATER_DETECTED(20),

    INVALID_LOGIN(2),
    INVALID_PASSWORD(3),
    USER_LOGIN_ALREADY_EXIST(5),

    MVD_PERSONAL_DATA_DOESNT_EXIST(6),
    MVD_TERRORIST_ERROR(7),
    MVD_TIME_OUT_ERROR(8);



    private int status;

    Errors(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
