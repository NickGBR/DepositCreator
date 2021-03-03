package ru.interns.deposit.external.deposit.dto;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OpenDepositRequestDTO {
    Long passportNumber;
}
