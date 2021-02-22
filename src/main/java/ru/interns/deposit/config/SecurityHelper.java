package ru.interns.deposit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;

@Configuration
public class SecurityHelper {

    @Bean
    public PasswordEncoder encoder (){
        return new BCryptPasswordEncoder(12);
    }
}
