package ru.interns.deposit.external.mvd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.DepositRequestDTO;
import ru.interns.deposit.external.mvd.dto.MvdRequestDTO;
import ru.interns.deposit.external.mvd.dto.CheckingInfo;
import ru.interns.deposit.external.mvd.enums.CheckType;
import ru.interns.deposit.external.mvd.service.MVDService;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.Status;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
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

            final CheckingInfo checkingInfo = UserCheckingRequestsInfo.result.get(login);

            getStatus(jsonObject, checkingInfo);

            checkAndOpenDepositForUserByUuid(uuid, login);
            correlationIdList.remove(uuid);
        }
    }

    @Override
    @SneakyThrows
    public void checkUser(UserDTO userDTO) {
        final MvdRequestDTO mvdRequestDTO = MvdRequestDTO.builder()
                .checkTypeCode(CheckType.DEFAULT_CHECK_ALL.getCode())
                .dateOfBirthday(userDTO.getDateOfBirthday())
                .kladrAddress(userDTO.getKladrAddress())
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .passportNumber(userDTO.getPassportNumber())
                .build();

        final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        final String json = ow.writeValueAsString(mvdRequestDTO);

        final Message message = new ActiveMQMessage();
        message.setStringProperty("JSONClient", json);
        message.setJMSCorrelationID(userDTO.getUuid().toString());

        jmsTemplate.convertAndSend(DESTINATION_NAME_CONSUMER, message);

        correlationIdList.add(userDTO.getUuid());
    }

    private void checkAndOpenDepositForUserByUuid(UUID uuid, String login) {
        final PersonalData personalData = personalDataService.getPersonalByForeignKey(userService.
                getUserByLogin(login).getId());

        final DepositRequestDTO requestDTO = DepositRequestDTO.builder()
                .passportNumber(personalData.getPassportNumber())
                .uuid(uuid)
                .build();
        depositService.checkAndOpen(requestDTO);
    }

    private void getStatus(JSONObject response, CheckingInfo checkingInfo) {
        final String status = response.getString("checkingStatus");
        switch (status) {
            case "SUCCESS":
                checkingInfo.getServiceStatus().put(Services.MVD, Status.SUCCESS);
                break;
            case "WAITING":
                checkingInfo.getServiceStatus().put(Services.MVD, Status.WAITING);
                break;
            case "CHECKING_FAILED":
                checkingInfo.getServiceStatus().put(Services.MVD, Status.CHECKING_FAILED);
                getErrors(response.getJSONArray("mvdErrorsList"),
                        checkingInfo.getErrors());
                break;
            default:
                log.info("Неизвестный статус от МВД сервера");
                break;
        }
    }

    private void getErrors(JSONArray mvdErrors, List<Errors> errors) {
        for (Object error : mvdErrors) {
            switch (error.toString()) {
                case "PERSONAL_DATA_DOESNT_EXIST":
                    errors.add(Errors.MVD_PERSONAL_DATA_DOESNT_EXIST);
                    break;
                case "TERRORIST_ERROR":
                    errors.add(Errors.MVD_TERRORIST_ERROR);
                    break;
                case "TIME_OUT_ERROR":
                    errors.add(Errors.MVD_TIME_OUT_ERROR);
                    break;
                default:
                    log.info("Получена неизвестная ошибка от МВД.");
            }
        }
    }
}
