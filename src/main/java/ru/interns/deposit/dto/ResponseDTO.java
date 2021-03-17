package ru.interns.deposit.dto;

import lombok.Builder;
import lombok.Data;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.enums.Status;
import ru.interns.deposit.service.enums.Errors;

import java.util.*;

@Data
@Builder
public class ResponseDTO {
    private Integer status;
    private List<Errors> errors;

/*    public static ResponseDTO toResponseDTO(ApacheIgniteDTO apacheIgniteDTO) {
        for (Map.Entry<Services, Status> pair : apacheIgniteDTO.getServiceStatus().entrySet()) {
            if (pair.getValue() == Status.WAITING){
                return ResponseDTO.builder()
                        .status(Status.WAITING.getStatus())
                        .build();
            } else if (pair.getValue() == Status.CHECKING_FAILED) {
                return ResponseDTO.builder()
                        .status(Status.CHECKING_FAILED.getStatus())
                        .errors(apacheIgniteDTO.getErrors())
                        .build();
            }
        }
        return ResponseDTO.builder()
                .status(Status.SUCCESS.getStatus())
                .build();
    }*/
}
