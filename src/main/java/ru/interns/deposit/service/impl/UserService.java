package ru.interns.deposit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.interns.deposit.db.dao.User;
import ru.interns.deposit.db.repositoiry.UsersRepository;
import ru.interns.deposit.dto.AuthenticationRequestDTO;
import ru.interns.deposit.dto.RegistrationDTO;
import ru.interns.deposit.security.JwtTokenProvider;
import ru.interns.deposit.security.enums.Role;
import ru.interns.deposit.security.enums.Status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UsersRepository usersRepository,
                       PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;

    }


    public boolean existByLogin(String login) {
        return usersRepository.existsByLogin(login);
    }

    public void addUser(RegistrationDTO registration) {
        User data = new User();
        data.setLogin(registration.getLogin());
        data.setPassword(passwordEncoder.encode(registration.getPassword()));
        data.setRole(Role.USER);
        data.setStatus(Status.ACTIVE);
        usersRepository.save(data);
    }

    public User getUserByLogin(String login){
        return usersRepository.findByLogin(login).orElseThrow(()
                -> new UsernameNotFoundException("User doesn't exist."));
    }
}
