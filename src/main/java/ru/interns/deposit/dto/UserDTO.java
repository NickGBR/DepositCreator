package ru.interns.deposit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {
    private String email;
    private String name;
    private String surname;
    private String middleName;
    private Long passportNumber;
    private Date dateOfBirthday;
    private Long address;

    @Override
    public String toString() {
        return "UserDTO{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", passportNumber=" + passportNumber +
                ", dateOfBirthday=" + dateOfBirthday +
                ", address=" + address +
                '}';
    }
}

