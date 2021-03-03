package ru.interns.deposit.external.deposit;

import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.security.enums.Status;
import ru.interns.deposit.service.enums.DepositServiceStatus;

public interface DepositService {

    DepositServiceStatus open(UserDTO userDTO);
}
