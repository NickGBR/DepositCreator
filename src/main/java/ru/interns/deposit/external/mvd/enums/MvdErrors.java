package ru.interns.deposit.external.mvd.enums;

public enum MvdErrors {
    PERSONAL_DATA_DOESNT_EXIST(1),
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
