package ru.interns.deposit.external.cheater.service.impl;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import ru.interns.cheaterWebService.wsdl.GetCheaterRequest;
import ru.interns.cheaterWebService.wsdl.GetCheaterResponse;

public class CheaterServiceClient extends WebServiceGatewaySupport {


    public GetCheaterResponse isCheater(Long passport) {

        GetCheaterRequest request = new GetCheaterRequest();
        request.setPassport(passport);


        GetCheaterResponse response = (GetCheaterResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:9999/ws/cheaters", request,
                        new SoapActionCallback(
                                "http://interns.ru/chaters/gs-producing-web-service/GetCheaterRequest"));

        return response;
    }

}