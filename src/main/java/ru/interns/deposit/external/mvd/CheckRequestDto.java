package ru.interns.deposit.external.mvd;

import lombok.Builder;
import lombok.Data;
import ru.interns.deposit.external.mvd.enums.RequestStatus;

import java.util.Date;

@Builder
@Data
public class CheckRequestDto {
    private RequestStatus status;
    private String name;
    private String surname;
    private String middleName;
    private Long passportNumber;
    private Date dateOfBirthday;
    private String address;
}
