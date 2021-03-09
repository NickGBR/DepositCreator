package ru.interns.deposit.external.deposit.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.db.temprorary.MvdStatus;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.DepositDTO;
import ru.interns.deposit.external.deposit.dto.DepositRequestDTO;
import ru.interns.deposit.external.deposit.dto.DepositResponseDTO;
import ru.interns.deposit.external.enums.RequestStatus;
import ru.interns.deposit.util.Api;
import java.util.*;

@Slf4j
@Component
public class DepositServiceImpl implements DepositService {
    public DepositResponseDTO checkAndOpen(DepositRequestDTO request) {
        System.out.println(LoginInfoService.data.get(request.getUuid()));
        if (MvdStatus.mvdCheckResult.get(LoginInfoService.data.get(request.getUuid())).getMvdErrorsList() == null) {
            open(request);
        }
        return null;
    }


    @Override
    public DepositResponseDTO getDeposits(Long passportNumber) {
        final DepositRequestDTO request = DepositRequestDTO.builder()
                .passportNumber(passportNumber)
                .build();
        get(request);
        return get(request);
    }

    @SneakyThrows
    private DepositResponseDTO open(DepositRequestDTO requestDTO) {
        final HttpClient httpClient = HttpClientBuilder.create().build();
        ObjectMapper jsonMapper = new ObjectMapper();
        final String jsonString = jsonMapper.writeValueAsString(requestDTO);

        StringEntity jsonEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(Api.OPEN_DEPOSIT_MOCK_POST_REQUEST.getUrl());
        post.setEntity(jsonEntity);
        post.setHeader("Content-type", "application/json");
        final HttpResponse execute = httpClient.execute(post);
        JSONObject jsonObject = new JSONObject(EntityUtils.toString(execute.getEntity()));
        return null;
    }

    @SneakyThrows
    private DepositResponseDTO get(DepositRequestDTO requestDTO) {
        final HttpClient httpClient = HttpClientBuilder.create().build();
        ObjectMapper jsonMapper = new ObjectMapper();
        final String jsonString = jsonMapper.writeValueAsString(requestDTO);

        StringEntity jsonEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);

        HttpPost post = new HttpPost(Api.GET_DEPOSITS_MOCK_POST_REQUEST.getUrl());
        post.setEntity(jsonEntity);
        post.setHeader("Content-type", "application/json");
        final HttpResponse execute = httpClient.execute(post);
        final JSONObject response = new JSONObject(EntityUtils.toString(execute.getEntity()));
        return handleResponse(response);
    }

    private DepositResponseDTO handleResponse(JSONObject response){
        String status = response.getString("status");
        switch (status) {
            case ("ERROR"):
                return getErrors(response);
            case ("DEPOSITS_LIST"):
                return getDepositsList(response);
            case ("USER_DOESNT_HAVE_DEPOSITS"):
                return DepositResponseDTO.builder()
                        .status(RequestStatus.DEPOSITS_DONT_EXIST)
                        .build();
            default:
                return null;
        }
    }

    private DepositResponseDTO getErrors(JSONObject response) {
        final JSONArray errorsJson = response.getJSONArray("errors");
        List<String> errors = new ArrayList<>();
        for (Object error : errorsJson) {
            errors.add(error.toString());
        }
        return DepositResponseDTO.builder()
                .errors(errors)
                .status(RequestStatus.ERROR)
                .build();
    }

    private DepositResponseDTO getDepositsList(JSONObject response) {
        final List<DepositDTO> depositDTOS = new ArrayList<>();
        final JSONArray deposits = response.getJSONArray("deposits");
        final DepositResponseDTO depositResponseDTO = DepositResponseDTO.builder().build();
        depositResponseDTO.setStatus(RequestStatus.GOT_DEPOSITS_SUCCESSFULLY);

        for (Object deposit : deposits) {
            JSONObject depositJson = new JSONObject(deposit.toString());
            final DepositDTO depositDTO = DepositDTO.builder()
                    .depositAmount(depositJson.getString("depositAmount"))
                    .depositNumber(depositJson.getString("depositNumber"))
                    .build();
            depositDTOS.add(depositDTO);
        }
        depositResponseDTO.setDeposits(depositDTOS);
        return depositResponseDTO;
    }
}
