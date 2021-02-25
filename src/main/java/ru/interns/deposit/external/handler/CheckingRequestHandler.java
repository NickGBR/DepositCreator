package ru.interns.deposit.external.handler;

import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.enums.CheckingStatus;

public interface CheckingRequestHandler {
    CheckingStatus checkUser(UserDTO userDTO);
}
