package ru.interns.deposit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import ru.interns.deposit.db.dao.User;
import ru.interns.deposit.security.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityService {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public SecurityService(JwtTokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public Map<String, String> getJwtTokenForUser(User user) {
        String token = tokenProvider.createToken(user.getLogin(), user.getRole().name());
        Map<String, String> response = new HashMap<>();
        response.put("login", user.getLogin());
        response.put("token", token);
        return response;
    }

    public void authenticateUser(String login, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login,
                password));
    }
}
