package ru.interns.deposit.external.mvd.service.impl;

import lombok.SneakyThrows;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQMessage;
import org.springframework.stereotype.Component;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.db.temprorary.MvdStatus;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.mvd.dto.MvdRequestDTO;
import ru.interns.deposit.external.mvd.enums.CheckType;
import ru.interns.deposit.external.mvd.service.MVDService;


@Component
public class MVDServiceImpl implements MVDService {

    @Override
    @SneakyThrows
    public void checkUser(UserDTO userDTO) {
        MvdRequestDTO mvdRequestDTO = MvdRequestDTO.builder()
                .checkTypeCode(CheckType.DEFAULT_CHECK_ALL.getCode())
                .dateOfBirthday(userDTO.getDateOfBirthday())
                .kladrAddress(userDTO.getKladrAddress())
                .name(userDTO.getName())
                .passportNumber(userDTO.getPassportNumber())
                .surname(userDTO.getSurname())
                .build();
        Message message = new ActiveMQMessage();
        message.setStringProperty("userJson", "ffdf");
        message.setJMSCorrelationID(userDTO.getUuid().toString());

        final String login = LoginInfoService.data.get(userDTO.getUuid());

//        MvdStatus.mvdCheckResult.get(login).setMvdErrorsList();
//        MvdStatus.mvdCheckResult.get(login).setCheckingStatus();

    }
}
