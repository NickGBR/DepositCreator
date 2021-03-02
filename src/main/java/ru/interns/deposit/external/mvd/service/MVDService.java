package ru.interns.deposit.external.mvd.service;

import org.springframework.stereotype.Component;
import ru.interns.deposit.dto.UserDTO;

public interface MVDService {
    void checkUser(UserDTO userDTO);
}
