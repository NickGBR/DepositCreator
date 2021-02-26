package ru.interns.deposit.external.mvd.dto;

import lombok.*;
import ru.interns.deposit.external.mvd.enums.CheckType;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class MvdRequestDTO {
    private String name;
    private String surname;
    private Long passportNumber;
    private Date dateOfBirthday;
    private Long kladrAddress;
    private int checkTypeCode;

    @Override
    public String toString() {
        return "MvdRequestDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", passportNumber=" + passportNumber +
                ", dateOfBirthday=" + dateOfBirthday +
                ", kladrAddress=" + kladrAddress +
                '}';
    }
}
