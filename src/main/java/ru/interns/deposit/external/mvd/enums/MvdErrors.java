package ru.interns.deposit.external.mvd.enums;

public enum MvdErrors {
    INCORRECT_NAME(1),
    INCORRECT_ADDRESS(2),
    INCORRECT_SURNAME(3),
    INCORRECT_PASSPORT_NUMBER(4),
    TERRORIST_ERROR(5),
    TIME_OUT_ERROR(6);

    private int code;

    MvdErrors(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
