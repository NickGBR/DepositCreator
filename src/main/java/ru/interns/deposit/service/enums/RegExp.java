package ru.interns.deposit.service.enums;

public enum RegExp {
    USER_NAME("([А-Яа-я][а-я- ]+[а-я])"),
    USER_SURNAME("([А-Яа-я][а-я- ]+[а-я])"),
    USER_MIDDLE_NAME("([А-Яа-я][а-я- ]+[а-я])"),
    USER_PASSPORT_NUMBER("([0-9]+)");

    final String regEx;

    RegExp(String regEx) {
        this.regEx = regEx;
    }

    public String get() {
        return regEx;
    }
}
