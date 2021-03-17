package ru.interns.deposit.external.mvd.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.ignite.Ignite;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.cache.CacheEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.db.temprorary.LoginInfoService;
//import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
import ru.interns.deposit.dto.ApacheIgniteDTO;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.DepositRequestDTO;
import ru.interns.deposit.external.enums.Status;
import ru.interns.deposit.external.mvd.dto.MvdRequestDTO;
//import ru.interns.deposit.external.mvd.dto.CheckingInfo;
import ru.interns.deposit.external.mvd.enums.CheckType;
import ru.interns.deposit.external.mvd.service.MVDService;
import ru.interns.deposit.external.mvd.thread.CheckTimeOut;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;
import ru.interns.deposit.util.Cache;

import javax.jms.JMSException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class MVDServiceImpl implements MVDService {
    private static final String DESTINATION_NAME_LISTENER = "response";
    private static final String DESTINATION_NAME_CONSUMER = "request";
    public static final Map<UUID, Date> sendDateMap = new HashMap<>();

    private final JmsTemplate jmsTemplate;
    private final DepositService depositService;
    private final UserService userService;
    private final PersonalDataService personalDataService;

    @Autowired
    private Ignite ignite;

    //запускаем поток для проверки time_out сообщений
    {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new CheckTimeOut());
    }

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

        if (sendDateMap.containsKey(uuid)) {
            final String login = LoginInfoService.data.get(uuid);

            // CheckingInfo checkingInfo = UserCheckingRequestsInfo.result.get(login);

            getStatus(jsonObject,uuid, login);

            checkAndOpenDepositForUserByUuid(uuid, login);
            sendDateMap.remove(uuid);
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

        sendDateMap.put(userDTO.getUuid(), new Date());
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

    private void getStatus(JSONObject response, UUID uuid, String login) {
        final String status = response.getString("checkingStatus");
        //IgniteCache<UUID, ClientDTO> cache = ignite.cache("my-cache2");
        switch (status) {
            case "SUCCESS":
                ApacheIgniteDTO apacheIgniteDTO = Cache.cache.get(login);
                apacheIgniteDTO.getServiceStatus().put(Services.MVD, Status.SUCCESS);
                Cache.cache.replace(login, apacheIgniteDTO);

                //checkingInfo.getServiceStatus().put(Services.MVD, Status.SUCCESS);
                break;
            case "WAITING":
                ApacheIgniteDTO apacheIgniteDTO2 = (ApacheIgniteDTO) ignite.cache("my-cache2").get(login);
                apacheIgniteDTO2.getServiceStatus().put(Services.MVD, Status.WAITING);
                Cache.cache.replace(login, apacheIgniteDTO2);
                //checkingInfo.getServiceStatus().put(Services.MVD, Status.WAITING);
                break;
            case "CHECKING_FAILED":
                System.out.println(ignite.cache("my-cache2").get(login));
                //ApacheIgniteDTO apacheIgniteDTO3 = (ApacheIgniteDTO) ignite.cache("my-cache2").get(login);
                //CacheEntry<Object, Object> entry = ignite.cache("my-cache2").getEntry(login);
                //BinaryObject bo = (BinaryObject) ignite.cache("my-cache2").get(login);
                //ApacheIgniteDTO apacheIgniteDTO3 = bo.deserialize();
                //ApacheIgniteDTO apacheIgniteDTO3 = (ApacheIgniteDTO) entry.getValue();
                //ClientDTO clientDTO3 = Cache.cache.get(login);
                BinaryObject bo = (BinaryObject) ignite.cache("my-cache2").get(login);
                ApacheIgniteDTO apacheIgniteDTO3 =  bo.deserialize();
                apacheIgniteDTO3.getServiceStatus().put(Services.MVD, Status.CHECKING_FAILED);

                //checkingInfo.getServiceStatus().put(Services.MVD, Status.CHECKING_FAILED);
                getErrors(response.getJSONArray("mvdErrorsList"),
                        apacheIgniteDTO3.getErrors());
                Cache.cache.replace(login, apacheIgniteDTO3);

                break;
            default:
                log.info("Неизвестный статус от МВД сервера");
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
