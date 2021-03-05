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
import ru.interns.deposit.external.mvd.dto.MvdResultCheckingDTO;
import ru.interns.deposit.external.mvd.enums.CheckType;
import ru.interns.deposit.external.mvd.enums.MvdErrors;
import ru.interns.deposit.external.mvd.service.MVDService;
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
    private static final List<UUID> correlationIdList = new ArrayList<>();

    private final JmsTemplate jmsTemplate;
    private final DepositService depositService;
    private final UserService userService;
    private final PersonalDataService personalDataService;

    @Autowired
    public MVDServiceImpl(JmsTemplate jmsTemplate, DepositService depositService, UserService userService, PersonalDataService personalDataService) {
        this.jmsTemplate = jmsTemplate;
        this.depositService = depositService;
        this.userService = userService;
        this.personalDataService = personalDataService;
    }

    @JmsListener(destination = DESTINATION_NAME_LISTENER)
    public void processMessages(Message message) throws JMSException, JSONException {
        final String jsonBody = message.getStringProperty("JSONClient");
        final JSONObject jsonObject = new JSONObject(jsonBody);

        final String correlationID = message.getJMSCorrelationID();
        final UUID uuid = UUID.fromString(correlationID);

        if (correlationIdList.contains(uuid)) {
            final String login = LoginInfoService.data.get(uuid);

            JSONArray errorsList = jsonObject.getJSONArray("mvdErrorsList");
            CheckingStatus checkingStatus = jsonObject.getEnum(CheckingStatus.class, "checkingStatus");

            List<MvdErrors> mvdErrors = new ArrayList<>();
            errorsList.forEach(x -> mvdErrors.add(MvdErrors.valueOf(x.toString())));

            MvdResultCheckingDTO mvdResultCheckingDTO = MvdStatus.mvdCheckResult.get(login);
            if (mvdResultCheckingDTO != null) {
                mvdResultCheckingDTO.setMvdErrorsList(mvdErrors);
                mvdResultCheckingDTO.setCheckingStatus(checkingStatus);
            }

            checkAndOpenDepositForUserByUuid(uuid);
            correlationIdList.remove(uuid);
        }
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

        correlationIdList.add(userDTO.getUuid());
    }

    private void checkAndOpenDepositForUserByUuid(UUID uuid) {
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
