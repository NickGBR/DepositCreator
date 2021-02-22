package ru.interns.deposit.dto;

import lombok.Data;

@Data
public class RegistrationDTO {
    String login;
    String password;

    @Override
    public String toString() {
        return "RegistrationDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
