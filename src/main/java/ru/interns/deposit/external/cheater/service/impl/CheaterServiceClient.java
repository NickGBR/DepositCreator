package ru.interns.deposit.external.cheater.service.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import ru.interns.cheaterWebService.wsdl.GetCheaterRequest;
import ru.interns.cheaterWebService.wsdl.GetCheaterResponse;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.cheater.service.CheaterService;
import ru.interns.deposit.external.mvd.dto.CheckingInfo;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.Status;

@Service
@Primary
public class CheaterServiceClient extends WebServiceGatewaySupport implements CheaterService {


    public void isCheater(UserDTO userDTO) {
        final CheckingInfo checkingInfo = UserCheckingRequestsInfo.result.get(LoginInfoService.data.get(userDTO.getUuid()));
        GetCheaterRequest request = new GetCheaterRequest();
        request.setPassport(userDTO.getPassportNumber());


        GetCheaterResponse response = (GetCheaterResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:9999/ws/cheaters", request,
                        new SoapActionCallback(
                                "http://interns.ru/chaters/gs-producing-web-service/GetCheaterRequest"));
        if (response.getCode() == 0L) {
            checkingInfo.getServiceStatus().put(Services.CHEATER, Status.SUCCESS);
        }
        checkingInfo.getErrors().add(Errors.CHEATER_DETECTED);
        checkingInfo.getServiceStatus().put(Services.CHEATER, Status.CHECKING_FAILED);
    }

}