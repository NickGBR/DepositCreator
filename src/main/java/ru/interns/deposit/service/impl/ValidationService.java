package ru.interns.deposit.service.impl;

import org.springframework.stereotype.Component;
import ru.interns.deposit.service.enums.Errors;

import java.util.*;

@Component
public class ValidationService {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MAX_LOGIN_LENGTH = 20;

    public void validatePassword(String password, List<Errors> errors) {
        if (password.length() < MIN_PASSWORD_LENGTH ||
                password.length() > MAX_PASSWORD_LENGTH) errors.add(Errors.INVALID_PASSWORD);
    }

    public void validateUserName(String password, List<Errors> errors) {
        if (password.length() < MIN_LOGIN_LENGTH ||
                password.length() > MAX_LOGIN_LENGTH) errors.add(Errors.INVALID_USER_NAME);
    }


}
