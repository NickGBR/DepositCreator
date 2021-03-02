package ru.interns.deposit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.interns.deposit.db.temprorary.MvdStatus;
import ru.interns.deposit.external.enums.CheckingStatus;
import ru.interns.deposit.external.mvd.dto.MvdResultCheckingDTO;
import ru.interns.deposit.service.impl.UserService;

@RestController
@RequestMapping("/api/v1/user/check/")
public class CheckingController {

    private UserService userService;


    public CheckingController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mvd_check")
    ResponseEntity<CheckingStatus> getMvdCheckingResult(){

        final MvdResultCheckingDTO mvdResultCheckingDTO
                = MvdStatus.mvdCheckResult.get(userService.getCurrentUser().getLogin());

        if (mvdResultCheckingDTO.getCheckingStatus().equals(CheckingStatus.WAITING)){
            return ResponseEntity.ok(CheckingStatus.WAITING);
        }
        return null;
    }
}
