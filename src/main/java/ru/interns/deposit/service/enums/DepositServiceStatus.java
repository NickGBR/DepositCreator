package ru.interns.deposit.service.enums;

public enum DepositServiceStatus {
        SUCCESS(0),
        CHECKING_IS_NOT_COMPLETED(2),
        FAILED(3);

        private final int status;

        DepositServiceStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
}
