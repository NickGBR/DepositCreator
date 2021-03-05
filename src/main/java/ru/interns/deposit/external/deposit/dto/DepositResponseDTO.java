package ru.interns.deposit.external.deposit.dto;

import lombok.Builder;
import lombok.Data;
import ru.interns.deposit.external.enums.RequestStatus;
import java.util.*;

@Data
@Builder
public class DepositResponseDTO {
    RequestStatus status;
    List<DepositDTO> deposits;
    List<String> errors;
}
