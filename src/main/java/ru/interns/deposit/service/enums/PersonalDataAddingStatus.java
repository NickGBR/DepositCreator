package ru.interns.deposit.service.enums;

public enum PersonalDataAddingStatus {

    DATA_ALREADY_EXIST(0),
    ADDED_SUCCESSFULLY(1);

    private int status;

    PersonalDataAddingStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
