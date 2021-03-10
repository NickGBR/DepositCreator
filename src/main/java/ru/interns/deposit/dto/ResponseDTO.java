package ru.interns.deposit.dto;

import lombok.Builder;
import lombok.Data;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.Status;
import java.util.*;

@Data
@Builder
public class ResponseDTO {
    Integer status;
    List<Errors> errors;
}
