package ru.interns.deposit.service.impl;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.db.temprorary.MvdStatus;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.DepositRequestDTO;
import ru.interns.deposit.external.enums.CheckingStatus;
import ru.interns.deposit.external.mvd.dto.MvdResultCheckingDTO;
import ru.interns.deposit.external.mvd.enums.MvdErrors;
import ru.interns.deposit.service.DepositCheckerService;
import ru.interns.deposit.service.enums.OpenDepositCheckerStatus;

import java.util.*;

@Component
public class DepositCheckerServiceMock implements DepositCheckerService {

    private final DepositService depositService;

    @Autowired
    public DepositCheckerServiceMock(DepositService depositService) {
        this.depositService = depositService;
    }

    @Override
    @SneakyThrows
    public OpenDepositCheckerStatus checkAndOpen(UserDTO userDTO) {
        final String login = LoginInfoService.data.get(userDTO.getUuid());
        final MvdResultCheckingDTO mvdResultCheckingDTO = MvdStatus.mvdCheckResult.get(login);
        while (mvdResultCheckingDTO.getCheckingStatus().equals(CheckingStatus.WAITING)) {
            Random random = new Random();
            int check = random.nextInt(3) + 1;
            System.out.println(check);
            Thread.sleep(1000);

            switch (check) {
                case 1:
                    mvdResultCheckingDTO.setCheckingStatus(CheckingStatus.CHECKING_FAILED);
                    mvdResultCheckingDTO.setMvdErrorsList(getRandomErrors());
                    break;
                case 2:
                    mvdResultCheckingDTO.setCheckingStatus(CheckingStatus.SUCCESS);
                    DepositRequestDTO depositRequestDTO = DepositRequestDTO.builder()
                            .passportNumber(userDTO.getPassportNumber())
                            .uuid(userDTO.getUuid())
                            .build();
                    depositService.checkAndOpen(depositRequestDTO);
                    break;
                case 3:
                    mvdResultCheckingDTO.setCheckingStatus(CheckingStatus.WAITING);
                    break;
            }
            System.out.println(MvdStatus.mvdCheckResult.get(login).getCheckingStatus());
        }
        return null;
    }

    private List<MvdErrors> getRandomErrors() {
        List<MvdErrors> mvdErrorsList = new ArrayList<>();
        Random random = new Random();
        if (random.nextBoolean()) {
            mvdErrorsList.add(MvdErrors.PERSONAL_DATA_DOESNT_EXIST);
            return mvdErrorsList;
        }

        if (random.nextBoolean()) {
            mvdErrorsList.add(MvdErrors.TERRORIST_ERROR);
            return mvdErrorsList;
        }
        mvdErrorsList.add(MvdErrors.TIME_OUT_ERROR);
        return mvdErrorsList;
    }
}
