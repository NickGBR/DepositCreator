package ru.interns.deposit.external.deposit;

import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.dto.PersonalDataDTO;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.deposit.dto.OpenDepositRequestDTO;
import ru.interns.deposit.security.enums.Status;
import ru.interns.deposit.service.enums.DepositServiceStatus;

public interface DepositService {

    DepositServiceStatus checkAndOpen(OpenDepositRequestDTO requestDTO);
}
