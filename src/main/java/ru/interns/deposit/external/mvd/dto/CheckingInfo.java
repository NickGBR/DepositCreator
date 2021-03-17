package ru.interns.deposit.external.mvd.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.interns.deposit.dto.ResponseDTO;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.enums.Status;
import ru.interns.deposit.external.mvd.enums.MvdErrors;
import ru.interns.deposit.service.enums.Errors;

import java.util.*;

/*@Builder
@Getter
public class CheckingInfo {
    private Map<Services, Status> serviceStatus;
    private List<Errors> errors;

    public ResponseDTO toResponseDTO() {
        for (Map.Entry<Services, Status> pair : serviceStatus.entrySet()) {
            if (pair.getValue() == Status.WAITING){
                return ResponseDTO.builder()
                        .status(Status.WAITING.getStatus())
                        .build();
            }
        }
        for (Map.Entry<Services, Status> pair : serviceStatus.entrySet()) {
            if (pair.getValue() == Status.CHECKING_FAILED){
                return ResponseDTO.builder()
                        .status(Status.CHECKING_FAILED.getStatus())
                        .errors(errors)
                        .build();
            }
        }
        return ResponseDTO.builder()
                .status(Status.SUCCESS.getStatus())
                .build();
    }
}*/
