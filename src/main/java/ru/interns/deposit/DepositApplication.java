package ru.interns.deposit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.interns.cheaterWebService.wsdl.GetCheaterResponse;
import ru.interns.deposit.external.cheater.service.impl.CheaterServiceClient;

@SpringBootApplication
public class DepositApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepositApplication.class, args);
    }

    @Bean
    CommandLineRunner lookup(CheaterServiceClient quoteClient) {
        return args -> {
            Long country = 1111222722L;
            GetCheaterResponse response = quoteClient.isCheater(country);
            System.err.println(response.getCode());
        };
    }
}
