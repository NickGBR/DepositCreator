package ru.interns.deposit.external.mvd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.db.temprorary.MvdStatus;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.OpenDepositRequestDTO;
import ru.interns.deposit.external.enums.CheckingStatus;
import ru.interns.deposit.external.mvd.dto.MvdRequestDTO;
import ru.interns.deposit.external.mvd.enums.CheckType;
import ru.interns.deposit.external.mvd.enums.MvdErrors;
import ru.interns.deposit.external.mvd.service.MVDService;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class MVDServiceImpl implements MVDService {
    private static final String DESTINATION_NAME_LISTENER = "response";
    private static final String DESTINATION_NAME_CONSUMER = "request";

    private final JmsTemplate jmsTemplate;
    private final DepositService depositService;
    private final UserService userService;
    private final PersonalDataService personalDataService;
    private final PersonalDataMapper mapper;

    @Autowired
    public MVDServiceImpl(JmsTemplate jmsTemplate, DepositService depositService, UserService userService, PersonalDataService personalDataService, PersonalDataMapper personalDataMapper) {
        this.jmsTemplate = jmsTemplate;
        this.depositService = depositService;
        this.userService = userService;
        this.personalDataService = personalDataService;
        this.mapper = personalDataMapper;
    }

    @JmsListener(destination = DESTINATION_NAME_LISTENER)
    public void processMessages(Message message) throws JMSException, JSONException {
        String jsonBody = message.getStringProperty("JSONClient");
        JSONObject jsonObject = new JSONObject(jsonBody);
        List<MvdErrors> mvdErrors = new ArrayList<>();

        UUID uuid = UUID.fromString(jsonObject.get("uuid").toString());
        final String login = LoginInfoService.data.get(uuid);

        JSONArray errorsList = jsonObject.getJSONArray("errorsList");
        CheckingStatus checkingStatus = jsonObject.getEnum(CheckingStatus.class, "checkingStatus");

        errorsList.forEach(x -> mvdErrors.add((MvdErrors) x));

        MvdStatus.mvdCheckResult.get(login).setMvdErrorsList(mvdErrors);
        MvdStatus.mvdCheckResult.get(login).setCheckingStatus(checkingStatus);

        checkAndOpenDepositForUserByUuid(uuid);
    }

    @Override
    @SneakyThrows
    public void checkUser(UserDTO userDTO) {
        MvdRequestDTO mvdRequestDTO = MvdRequestDTO.builder()
                .checkTypeCode(CheckType.DEFAULT_CHECK_ALL.getCode())
                .dateOfBirthday(userDTO.getDateOfBirthday())
                .kladrAddress(userDTO.getKladrAddress())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .passportNumber(userDTO.getPassportNumber())
                .build();

        final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(mvdRequestDTO);

        Message message = new ActiveMQMessage();
        message.setStringProperty("JSONClient", json);
        message.setJMSCorrelationID(userDTO.getUuid().toString());

        jmsTemplate.convertAndSend(DESTINATION_NAME_CONSUMER, message);
    }

    private void checkAndOpenDepositForUserByUuid(UUID uuid){
        final PersonalData personalData =
                personalDataService.getPersonalByForeignKey(userService.
                        getUserByLogin(LoginInfoService.data.get(uuid)).getId());
        final OpenDepositRequestDTO requestDTO = OpenDepositRequestDTO.builder()
                .passportNumber(personalData.getPassportNumber())
                .uuid(uuid)
                .build();
        depositService.checkAndOpen(requestDTO);
    }
}
