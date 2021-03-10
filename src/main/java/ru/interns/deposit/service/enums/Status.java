package ru.interns.deposit.service.enums;

public enum Status {

    SUCCESS(0),
    ERROR(1);
    private final int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
