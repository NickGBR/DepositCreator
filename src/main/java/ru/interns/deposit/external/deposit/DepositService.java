package ru.interns.deposit.external.deposit;

import ru.interns.deposit.external.deposit.dto.DepositRequestDTO;
import ru.interns.deposit.external.deposit.dto.DepositResponseDTO;

public interface DepositService {

    DepositResponseDTO checkAndOpen(DepositRequestDTO requestDTO);

    DepositResponseDTO getDeposits(Long passportNumber);
}
