package ru.interns.deposit.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class PersonalDataDTO {
    private String name;
    private String surname;
    private String middleName;
    private Long passportNumber;
    private Date dateOfBirthday;
    private String address;
    private Long kladrAddress;

}
