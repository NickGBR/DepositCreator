package ru.interns.deposit.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.RegExp;

import java.util.*;

@Slf4j
@Component
public class ValidationService {

    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MAX_LOGIN_LENGTH = 20;

    private static final int MIN_USER_NAME_LENGTH = 1;
    private static final int MAX_USER_NAME_LENGTH = 50;

    private static final int MIN_USER_MIDDLE_NAME_LENGTH = 1;
    private static final int MAX_USER_MIDDLE_NAME_LENGTH = 50;

    private static final int MIN_USER_SURNAME_LENGTH = 1;
    private static final int MAX_USER_SURNAME_LENGTH = 50;

    private static final int PASSPORT_NUMBER_MIN_LENGTH = 9;
    private static final int PASSPORT_NUMBER_MAX_LENGTH = 10;


    public void validatePassword(String password, List<Errors> errors) {
        if (password.length() < MIN_PASSWORD_LENGTH ||
                password.length() > MAX_PASSWORD_LENGTH) {
            log.warn("Пароль не удоволетворяет критерию длины");
            errors.add(Errors.INVALID_PASSWORD);
        }
    }

    public void validateUserLogin(String password, List<Errors> errors) {
        if (password.length() < MIN_LOGIN_LENGTH ||
                password.length() > MAX_LOGIN_LENGTH){
            log.warn("Логин не удоволетворяет критерию длины");
            errors.add(Errors.INVALID_LOGIN);
        }
    }

    public void validateUserMiddleName(String name, List<Errors> errors) {
            if (name.length() < MIN_USER_MIDDLE_NAME_LENGTH) {
                log.warn("Отчество пользователя слишком короткое");
                errors.add(Errors.USER_MIDDLE_NAME_IS_TOO_SHORT);
            } else if (name.length() > MAX_USER_MIDDLE_NAME_LENGTH) {
                log.warn("Отчество пользователя слишком длинное");
                errors.add(Errors.USER_MIDDLE_NAME_IS_TOO_LONG);
            }
            if (!name.matches(RegExp.USER_MIDDLE_NAME.get())) {
                log.warn("Отчество пользователя содержит недопустимые символы");
                errors.add(Errors.USER_MIDDLE_NAME_IS_INVALID);
            }
    }

    public void validateUserName(String name, List<Errors> errors) {
        if (name.length() < MIN_USER_NAME_LENGTH) {
            log.warn("Имя пользователя слишком короткое");
            errors.add(Errors.USER_NAME_IS_TOO_SHORT);
        } else if (name.length() > MAX_USER_NAME_LENGTH) {
            log.warn("Имя пользователя слишком длинное");
            errors.add(Errors.USER_NAME_IS_TOO_LONG);
        }
        if (!name.matches(RegExp.USER_NAME.get())) {
            log.warn("Имя пользователя содержит недопустимые символы");
            errors.add(Errors.USER_NAME_IS_INVALID);
        }
    }

    public void validateUserSurname(String surname, List<Errors> errors) {
        if (surname.length() < MIN_USER_SURNAME_LENGTH) {
            log.warn("Фамилия пользователя не удоволетворяет критерию минимальной длины");
            errors.add(Errors.USER_SURNAME_IS_TOO_SHORT);
        } else if (surname.length() > MAX_USER_SURNAME_LENGTH) {
            log.warn("Фамилия пользователя не удоволетворяет критерию максимальной длины");
            errors.add(Errors.USER_SURNAME_IS_TOO_LONG);
        }
        if (!surname.matches(RegExp.USER_SURNAME.get())) {
            log.warn("Фамилия пользователя содержит недопустимые символы");
            errors.add(Errors.USER_SURNAME_IS_INVALID);
        }
    }

    public void validatePassport(Long passport, List<Errors> errors) {

        if (!passport.toString().matches(RegExp.USER_PASSPORT_NUMBER.get())) {
            log.warn("Номер паспорта содержит недопустмые символы");
            errors.add(Errors.PASSPORT_CONTAINS_WRONG_SYMBOLS);
        } else {
            if (passport.toString().length() < PASSPORT_NUMBER_MIN_LENGTH ||
                    passport.toString().length() > PASSPORT_NUMBER_MAX_LENGTH) {
                log.warn("Номер паспорта содержит недостатоное колличество символов");
                errors.add(Errors.PASSPORT_NUMBER_LENGTH);
            }
        }
    }

    public boolean validateDateOfBirthday(Date dateOfBirthday, List<Errors> errors) {
        Calendar now = new GregorianCalendar();
        Calendar date = new GregorianCalendar();
        date.setTime(dateOfBirthday);
        int age = now.get(Calendar.YEAR) - date.get(Calendar.YEAR);
        if (now.get(Calendar.MONTH) < date.get(Calendar.MONTH)) {
            age--;
        } else if (now.get(Calendar.MONTH) == date.get(Calendar.MONTH)) {
            if (now.get(Calendar.DAY_OF_MONTH) < date.get(Calendar.DAY_OF_MONTH)) {
                age--;
            }
        }
        if (age < 14) {
            log.warn("Пользователь младше 14 лет");
            errors.add(Errors.USER_AGE_IS_UNDER_14);
            return false;
        }
        return true;
    }
}
