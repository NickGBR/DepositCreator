package ru.interns.deposit.external.enums;

public enum CheckingStatus {

    SUCCESS(0),
    WAITING(1),
    CHECKING_FAILED(2);

    private final int status;

    CheckingStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

