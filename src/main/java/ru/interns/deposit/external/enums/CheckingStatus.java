package ru.interns.deposit.external.enums;

public enum CheckingStatus {

    SUCCESS(0),
    WAITING(1),
    TIMEOUT_ERROR(2),
    CHECKING_FAILED(3);

    private final int status;

    CheckingStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

