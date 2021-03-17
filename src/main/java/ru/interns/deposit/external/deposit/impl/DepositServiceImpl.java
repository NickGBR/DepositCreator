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
import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.DepositDTO;
import ru.interns.deposit.external.deposit.dto.DepositRequestDTO;
import ru.interns.deposit.external.deposit.dto.DepositResponseDTO;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.Status;
import ru.interns.deposit.util.Api;

import java.util.*;

@Slf4j
@Component
public class DepositServiceImpl implements DepositService {
    public DepositResponseDTO checkAndOpen(DepositRequestDTO request) {

        if (checkSuccess(UserCheckingRequestsInfo
                .result.get(LoginInfoService
                        .data.get(request.getUuid())).getServiceStatus())) open(request);
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

    private DepositResponseDTO handleResponse(JSONObject response) {
        String status = response.getString("status");
        switch (status) {
            case ("ERROR"):
                return getErrors(response);
            case ("SUCCESS"):
                return getDepositsList(response);
            default:
                return null;
        }
    }

    private DepositResponseDTO getErrors(JSONObject response) {
        final JSONArray errorsJson = response.getJSONArray("errors");
        List<Errors> errors = new ArrayList<>();
        getErrors(errorsJson, errors);
        return DepositResponseDTO.builder()
                .errors(errors)
                .status(Status.CHECKING_FAILED.getStatus())
                .build();
    }

    private DepositResponseDTO getDepositsList(JSONObject response) {
        final List<DepositDTO> depositDTOS = new ArrayList<>();
        final JSONArray deposits = response.getJSONArray("deposits");
        final DepositResponseDTO depositResponseDTO = DepositResponseDTO.builder().build();
        depositResponseDTO.setStatus(Status.SUCCESS.getStatus());

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

    private boolean checkSuccess(Map<Services, Status> statusMap) {
        for (Map.Entry<Services, Status> pair : statusMap.entrySet()) {
            if (pair.getValue() != Status.SUCCESS) {
                return false;
            }
        }
        return true;
    }

    private void getErrors(JSONArray depositErrors, List<Errors> errors) {
        for (Object depositError : depositErrors) {
            switch (depositError.toString()) {
                case "JSON_PARSE_ERROR":
                    errors.add(Errors.DEPOSIT_JSON_REQUEST_PARSE_ERROR);
                    break;
                case "USER_DOESNT_HAVE_DEPOSITS":
                    errors.add(Errors.DEPOSIT_USER_DOESNT_HAVE_DEPOSITS);
                default:
                    errors.add(Errors.DEPOSIT_UNKNOWN_ERROR);
                    break;
            }
        }
    }
}
