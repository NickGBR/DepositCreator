package ru.interns.deposit.external.deposit.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.db.temprorary.MvdStatus;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.OpenDepositRequestDTO;
import ru.interns.deposit.service.enums.DepositServiceStatus;
import ru.interns.deposit.util.Api;

@Slf4j
@Component
public class DepositServiceImpl implements DepositService {
    public DepositServiceStatus open(UserDTO userDTO){
//        if(MvdStatus.mvdCheckResult.get(LoginInfoService.data.get(userDTO.getUuid())).getMvdErrorsList().size() == 0){
//
//        }

        //Mock открывает счет без проверок
        log.info(userDTO.getPassportNumber().toString());
        open(OpenDepositRequestDTO.builder().passportNumber(userDTO.getPassportNumber()).build());
        return null;
    }

    @SneakyThrows
    private void open(OpenDepositRequestDTO requestDTO){
        final HttpClient httpClient = HttpClientBuilder.create().build();
        ObjectMapper jsonMapper = new ObjectMapper();
        final String jsonString = jsonMapper.writeValueAsString(requestDTO);

        StringEntity jsonEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(Api.OPEN_DEPOSIT_MOCK_POST_REQUEST.getUrl());
        post.setEntity(jsonEntity);
        post.setHeader("Content-type", "application/json");
        final HttpResponse execute = httpClient.execute(post);
        JSONObject response = new JSONObject(EntityUtils.toString(execute.getEntity()));
    }
}
