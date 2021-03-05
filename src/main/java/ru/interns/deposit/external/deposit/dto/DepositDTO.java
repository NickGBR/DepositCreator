package ru.interns.deposit.external.deposit.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DepositDTO {
    String depositNumber;
    String depositAmount;
}
