package ru.interns.deposit.external.deposit.dto;

import lombok.Builder;
import lombok.Data;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.Status;

import java.util.*;

@Data
@Builder
public class DepositResponseDTO {
    Integer status;
    List<DepositDTO> deposits;
    List<Errors> errors;
}
