package ru.interns.deposit.service;

import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.service.enums.OpenDepositCheckerStatus;

public interface OpenDepositCheckerService {
    OpenDepositCheckerStatus checkAndOpen(UserDTO userDTO);
}
