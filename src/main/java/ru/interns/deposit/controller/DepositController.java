package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.OpenDepositCheckerService;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;

@RestController
@RequestMapping("/api/v1/user/deposit/")
public class DepositController {


    private final OpenDepositCheckerService checkerService;
    private final PersonalDataMapper mapper;
    private final PersonalDataService personalDataService;

    @Autowired
    public DepositController(OpenDepositCheckerService checkerService, PersonalDataMapper mapper, PersonalDataService personalDataService) {
        this.checkerService = checkerService;
        this.mapper = mapper;
        this.personalDataService = personalDataService;
    }

    @GetMapping("/open")
    public ResponseEntity<?> openDeposit(){
        final UserDTO userDTO = mapper.toUserDto(personalDataService.get());
        System.out.println(userDTO);
        checkerService.checkAndOpen(userDTO);
        return ResponseEntity.ok("test");
    }
}
