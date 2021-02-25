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
import ru.interns.deposit.service.impl.SecurityService;
import ru.interns.deposit.service.impl.UserService;
import ru.interns.deposit.util.ClientErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("api/v1/auth/")
public class AuthorizationController {

    private final UserService userService;
    private final SecurityService securityService;

    @Autowired
    public AuthorizationController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }




    @PostMapping("/registration")
    public @ResponseBody
    ResponseEntity<?> create(@RequestBody RegistrationDTO registrationDTO) {
        if (userService.existByLogin(registrationDTO.getLogin())) {
            return new ResponseEntity<>(ClientErrorCode.USER_ALREADY_EXISTS, HttpStatus.CONFLICT);
        } else {
            userService.addUser(registrationDTO);
            return ResponseEntity.ok(true);
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
