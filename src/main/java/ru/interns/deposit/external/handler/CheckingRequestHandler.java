package ru.interns.deposit.external.handler;

import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.enums.Status;

public interface CheckingRequestHandler {
    Status checkUser(UserDTO userDTO);
}
