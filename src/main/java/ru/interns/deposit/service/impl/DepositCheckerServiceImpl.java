package ru.interns.deposit.service.impl;

import org.springframework.stereotype.Component;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.cheater.service.CheaterService;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.handler.CheckingRequestHandler;
import ru.interns.deposit.external.mvd.service.MVDService;
import ru.interns.deposit.service.DepositCheckerService;
import ru.interns.deposit.service.enums.OpenDepositCheckerStatus;

@Component
public class DepositCheckerServiceImpl implements DepositCheckerService {

    private final DepositService depositService;
    private final CheckingRequestHandler checkingRequestHandler;
    private final MVDService mvdService;
    private final CheaterService cheaterService;

    public DepositCheckerServiceImpl(DepositService depositService, CheckingRequestHandler checkingRequestHandler, MVDService mvdService, CheaterService cheaterService) {
        this.depositService = depositService;
        this.checkingRequestHandler = checkingRequestHandler;
        this.mvdService = mvdService;
        this.cheaterService = cheaterService;
    }

    @Override
    public OpenDepositCheckerStatus checkAndOpen(UserDTO userDTO) {
        mvdService.checkUser(userDTO);
        cheaterService.isCheater(userDTO);
        return null;

    }
}
