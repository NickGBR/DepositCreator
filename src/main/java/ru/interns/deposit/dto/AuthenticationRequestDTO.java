package ru.interns.deposit.dto;

import lombok.Data;

/**
 * На данном этапе данный класс выглядит
 * как {@see ru.interns.deposit.dto.ResponseDTO}
 * но так как возможно расширение этих классв,
 * в разные направления, сущности для регистрации и аутентификации
 * реализованны разными классами.
 */
@Data
public class AuthenticationRequestDTO {
private String login;
private String password;
}
