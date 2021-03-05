package ru.interns.deposit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class DepositApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepositApplication.class, args);

    }

}
