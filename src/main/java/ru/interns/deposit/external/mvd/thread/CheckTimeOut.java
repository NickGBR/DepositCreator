package ru.interns.deposit.external.mvd.thread;

import ru.interns.deposit.db.temprorary.LoginInfoService;
//import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.enums.Status;
//import ru.interns.deposit.external.mvd.dto.CheckingInfo;
import ru.interns.deposit.external.mvd.service.impl.MVDServiceImpl;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.util.Cache;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class CheckTimeOut implements Runnable {
    private static final int timeOutTime = 3;

    @Override
    public void run() {
        while (true) {
            Date now = new Date();
            for (Map.Entry<UUID, Date> messageInfo : MVDServiceImpl.sendDateMap.entrySet()) {
                if (now.getTime() - messageInfo.getValue().getTime() > timeOutTime * 1000) {
                    final String login = LoginInfoService.data.get(messageInfo.getKey());


/*                    final CheckingInfo checkingInfo = UserCheckingRequestsInfo.result.get(login);
                    checkingInfo.getServiceStatus().put(Services.MVD, Status.CHECKING_FAILED);
                    List<Errors> errors = checkingInfo.getErrors();
                    errors.add(Errors.MVD_TIME_OUT_ERROR);*/

                    Cache.setServiceStatus(login, Services.MVD, Status.CHECKING_FAILED);
                    Cache.addErrors(login, Errors.MVD_TIME_OUT_ERROR);
                    MVDServiceImpl.sendDateMap.remove(messageInfo.getKey());
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
