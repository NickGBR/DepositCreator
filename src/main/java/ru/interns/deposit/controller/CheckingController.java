package ru.interns.deposit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
import ru.interns.deposit.external.enums.Status;
import ru.interns.deposit.external.mvd.dto.CheckingInfo;
import ru.interns.deposit.service.impl.UserService;

@RestController
@RequestMapping("/api/v1/user/check/")
public class CheckingController {

    private UserService userService;


    public CheckingController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mvd_check")
    ResponseEntity<Status> getMvdCheckingResult(){

        final CheckingInfo checkingInfo
                = UserCheckingRequestsInfo.result.get(userService.getCurrentUser().getLogin());

        if (checkingInfo.getServiceStatus().equals(Status.WAITING)){
            return ResponseEntity.ok(Status.WAITING);
        }
        return null;
    }
}
