package ru.interns.deposit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.mvd.service.impl.MVDServiceImpl;

import java.util.Date;
import java.util.UUID;

@SpringBootApplication
@EnableJms
public class DepositApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(DepositApplication.class, args);
/*        MVDServiceImpl mvdService = run.getBean(MVDServiceImpl.class);
        UserDTO userDTO = UserDTO.builder()
                .passportNumber(77777777L)
                .middleName("aaaa")
                .surname("aaaaa")
                .uuid(UUID.randomUUID())
                .dateOfBirthday(new Date())
                .kladrAddress(8888888888888888888L)
                .build();
        mvdService.checkUser(userDTO);*/
    }
}
