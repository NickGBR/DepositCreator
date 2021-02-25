package ru.interns.deposit.service.enums;

public enum DepositServiceStatus {
        SUCCESS(0),
        FAILED(3);

        private final int status;

        DepositServiceStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }
}
