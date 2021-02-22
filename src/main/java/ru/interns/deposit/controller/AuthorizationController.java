package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.interns.deposit.db.dao.User;
import ru.interns.deposit.db.repositoiry.UsersRepository;
import ru.interns.deposit.dto.RegistrationDTO;
import ru.interns.deposit.security.enums.Role;
import ru.interns.deposit.security.enums.Status;

@RestController
@RequestMapping("api/auth/")
public class AuthorizationController {

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    @Autowired
    public AuthorizationController(PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    @PostMapping("user/registration")
    public @ResponseBody
    Boolean create(@RequestBody RegistrationDTO registrationDTO){
        if(usersRepository.existsByLogin(registrationDTO.getLogin())){
            return false;
            }
        else {
            User data = new User();
            data.setLogin(registrationDTO.getLogin());
            data.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
            data.setRole(Role.USER);
            data.setStatus(Status.ACTIVE);
            usersRepository.save(data);
            return true;
        }
    }
}
