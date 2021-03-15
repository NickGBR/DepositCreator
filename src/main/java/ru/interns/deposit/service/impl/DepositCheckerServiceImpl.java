package ru.interns.deposit.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.cheater.service.CheaterService;
import ru.interns.deposit.external.mvd.service.MVDService;
import ru.interns.deposit.service.DepositCheckerService;
import ru.interns.deposit.service.enums.OpenDepositCheckerStatus;

@Component
public class DepositCheckerServiceImpl implements DepositCheckerService {

    private final MVDService mvdService;
    private final CheaterService cheaterService;

    public DepositCheckerServiceImpl(MVDService mvdService, CheaterService cheaterService) {
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
