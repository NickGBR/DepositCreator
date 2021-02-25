package ru.interns.deposit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.enums.CheckingStatus;
import ru.interns.deposit.external.handler.CheckingRequestHandler;
import ru.interns.deposit.external.mvd.service.MVDService;
import ru.interns.deposit.service.DepositService;
import ru.interns.deposit.service.OpenDepositCheckerService;
import ru.interns.deposit.service.enums.DepositServiceStatus;
import ru.interns.deposit.service.enums.OpenDepositCheckerStatus;

public class OpenDepositCheckerServiceImpl implements OpenDepositCheckerService {

    private final DepositService depositService;
    private final CheckingRequestHandler checkingRequestHandler;

    @Autowired
    public OpenDepositCheckerServiceImpl(DepositService depositService, CheckingRequestHandler checkingRequestHandler) {
        this.depositService = depositService;
        this.checkingRequestHandler = checkingRequestHandler;
    }

    @Override
    public OpenDepositCheckerStatus checkAndOpen(UserDTO userDTO) {
        if(checkingRequestHandler.checkUser(userDTO).equals(CheckingStatus.SUCCESS)){
            if(depositService.open(userDTO).equals(DepositServiceStatus.SUCCESS)){
                return OpenDepositCheckerStatus.SUCCESS;
            }
        }
        return OpenDepositCheckerStatus.CHECKING_FAILED;
    }
}
