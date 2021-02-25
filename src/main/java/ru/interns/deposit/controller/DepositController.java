package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.service.OpenDepositCheckerService;

@Controller
public class DepositController {

    private OpenDepositCheckerService checkerService;

    @Autowired
    public DepositController(OpenDepositCheckerService checkerService) {
        this.checkerService = checkerService;
    }



    public ResponseEntity<?> openDeposit(UserDTO userDTO){
        checkerService.checkAndOpen(userDTO);
        return null;
    }

}
