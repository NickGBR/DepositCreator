package ru.interns.deposit.service;

import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.service.enums.OpenDepositCheckerStatus;

public interface DepositCheckerService {
    OpenDepositCheckerStatus checkAndOpen(UserDTO userDTO);
}
