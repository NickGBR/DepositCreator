package ru.interns.deposit.external.enums;

public enum Status {

    SUCCESS(0),
    WAITING(1),
    CHECKING_FAILED(2);

    private final int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

