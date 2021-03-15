package ru.interns.deposit.service.impl;

import org.springframework.stereotype.Component;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.mvd.service.MVDService;
import ru.interns.deposit.service.DepositCheckerService;
import ru.interns.deposit.service.enums.OpenDepositCheckerStatus;

@Component
public class DepositCheckerServiceImpl implements DepositCheckerService {

    private final MVDService mvdService;

    public DepositCheckerServiceImpl(MVDService mvdService) {
        this.mvdService = mvdService;
    }

    @Override
    public OpenDepositCheckerStatus checkAndOpen(UserDTO userDTO) {
        mvdService.checkUser(userDTO);
        return null;
    }
}
