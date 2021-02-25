package ru.interns.deposit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.handler.CheckingRequestHandler;
import ru.interns.deposit.service.DepositService;
import ru.interns.deposit.service.enums.DepositServiceStatus;

@Component
public class DepositServiceImpl implements DepositService {
    public DepositServiceStatus open(UserDTO userDTO){
        return null;
    }
}
