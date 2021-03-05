package ru.interns.deposit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.mvd.service.impl.MVDServiceImpl;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
public class DepositApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepositApplication.class, args);

    }

}
