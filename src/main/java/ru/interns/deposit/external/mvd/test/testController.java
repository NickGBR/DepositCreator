package ru.interns.deposit.external.mvd.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.Message;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.external.mvd.dto.MvdRequestDTO;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;

import javax.jms.JMSException;
import java.util.UUID;

@RestController
public class testController {

    private JmsTemplate jmsTemplate;
    private UserService userService;
    private PersonalDataService personalDataService;
    private PersonalDataMapper mapper;
    private final String queueName = "name";

    @Autowired
    public testController(JmsTemplate jmsTemplate, UserService userService,
                          PersonalDataService personalDataService,
                          PersonalDataMapper mapper) {
        this.jmsTemplate = jmsTemplate;
        this.userService = userService;
        this.personalDataService = personalDataService;
        this.mapper = mapper;
    }

    @GetMapping("/test_jms")
    public void test() throws JMSException, JsonProcessingException {
        ObjectMapper jsonMapper = new ObjectMapper();
        UUID uuid = UUID.randomUUID();
        Message message = new ActiveMQMessage();
        final PersonalData personalData = personalDataService.get();
        final MvdRequestDTO mvdRequestDTO = mapper.toMvdRequest(personalData);

        final String jsonString = jsonMapper.writeValueAsString(mvdRequestDTO);

        message.setJMSCorrelationID(uuid.toString());
        message.setStringProperty("JSONOBJECT", jsonString);
        jmsTemplate.convertAndSend(queueName, message);

        JSONObject jsonObject = new JSONObject(message.getStringProperty("JSONOBJECT"));
    }
}
