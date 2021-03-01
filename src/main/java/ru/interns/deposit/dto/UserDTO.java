package ru.interns.deposit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String surname;
    private String middleName;
    private Long passportNumber;
    private Date dateOfBirthday;
    private Long kladrAddress;
    private UUID uuid;

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", passportNumber=" + passportNumber +
                ", dateOfBirthday=" + dateOfBirthday +
                ", kladrAddress=" + kladrAddress +
                ", uuid=" + uuid +
                '}';
    }
}

