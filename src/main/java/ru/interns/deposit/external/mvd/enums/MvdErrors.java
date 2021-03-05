package ru.interns.deposit.external.mvd.enums;

public enum MvdErrors {
    PERSONAL_DATA_DOESNT_EXIST(1),
    TERRORIST_ERROR(2),
    TIME_OUT_ERROR(3);

    private int code;

    MvdErrors(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
