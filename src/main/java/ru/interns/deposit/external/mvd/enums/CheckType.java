package ru.interns.deposit.external.mvd.enums;

public enum CheckType {
    DEFAULT_CHECK_ALL(0),
    CHECK_BY_PASSPORT_AND_ADDRESS(1),
    CHECK_BY_NAME_AND_PASSPORT(2),
    CHECK_BY_NAME_AND_ADDRESS(3);

    private int code;

    CheckType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
