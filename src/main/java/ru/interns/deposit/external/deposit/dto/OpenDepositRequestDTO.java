package ru.interns.deposit.external.deposit.dto;


import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class OpenDepositRequestDTO {
    Long passportNumber;
    UUID uuid;
}
