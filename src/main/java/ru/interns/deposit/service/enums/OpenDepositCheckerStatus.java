package ru.interns.deposit.service.enums;

public enum OpenDepositCheckerStatus {
    SUCCESS(0),
    WAITING(1),
    TIMEOUT_ERROR(2),
    CHECKING_FAILED(3);

    private final int status;

    OpenDepositCheckerStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
