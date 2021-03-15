package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import ru.interns.deposit.db.dao.User;
import ru.interns.deposit.dto.AuthenticationRequestDTO;
import ru.interns.deposit.dto.RegistrationDTO;
import ru.interns.deposit.dto.ResponseDTO;
import ru.interns.deposit.service.enums.Errors;

import java.util.*;

import ru.interns.deposit.service.enums.Status;
import ru.interns.deposit.service.impl.SecurityService;
import ru.interns.deposit.service.impl.UserService;
import ru.interns.deposit.service.impl.ValidationService;
import ru.interns.deposit.util.ClientErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthorizationController {

    private final UserService userService;
    private final SecurityService securityService;
    private final ValidationService validationService;

    @Autowired
    public AuthorizationController(UserService userService, SecurityService securityService, ValidationService validationService) {
        this.userService = userService;
        this.securityService = securityService;
        this.validationService = validationService;
    }


    @PostMapping("/registration")
    public @ResponseBody
    ResponseEntity<ResponseDTO> create(@RequestBody RegistrationDTO registrationDTO) {
        List<Errors> errors = new ArrayList<>();
        userService.existByLogin(registrationDTO.getLogin(), errors);
        validationService.validatePassword(registrationDTO.getPassword(), errors);
        validationService.validateUserLogin(registrationDTO.getLogin(), errors);
        if (errors.size() != 0) {
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status(Status.CHECKING_FAILED.getStatus())
                    .errors(errors)
                    .build());
        } else {
            userService.addUser(registrationDTO);
            return ResponseEntity.ok(ResponseDTO.builder()
                    .status(Status.SUCCESS.getStatus())
                    .build());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        try {
            securityService.authenticateUser(request.getLogin(), request.getPassword());
            final User user = userService.getUserByLogin(request.getLogin());
            return ResponseEntity.ok(securityService.getJwtTokenForUser(user));
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(ClientErrorCode.INVALID_LOGIN_PASSWORD, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
