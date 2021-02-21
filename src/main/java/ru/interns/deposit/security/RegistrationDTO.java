package ru.interns.deposit.security;

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
