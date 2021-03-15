package ru.interns.deposit.external.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.cheater.service.CheaterService;
import ru.interns.deposit.external.enums.Status;
import ru.interns.deposit.external.handler.CheckingRequestHandler;
import ru.interns.deposit.external.mvd.service.MVDService;

@Component
public class CheckingRequestHandlerImpl implements CheckingRequestHandler {
    private MVDService mvdService;
    private CheaterService cheaterService;


    @Autowired
    public CheckingRequestHandlerImpl(MVDService mvdService, CheaterService cheaterService) {
        this.mvdService = mvdService;
        this.cheaterService = cheaterService;
    }

    @Override
    public Status checkUser(UserDTO user) {
        return null;
    }
}
