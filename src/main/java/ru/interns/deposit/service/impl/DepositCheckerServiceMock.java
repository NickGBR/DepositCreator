//package ru.interns.deposit.service.impl;
//
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import ru.interns.deposit.db.temprorary.LoginInfoService;
//import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
//import ru.interns.deposit.dto.UserDTO;
//import ru.interns.deposit.external.deposit.DepositService;
//import ru.interns.deposit.external.deposit.dto.DepositRequestDTO;
//import ru.interns.deposit.external.enums.Status;
//import ru.interns.deposit.external.mvd.dto.CheckingInfo;
//import ru.interns.deposit.external.mvd.enums.MvdErrors;
//import ru.interns.deposit.service.DepositCheckerService;
//import ru.interns.deposit.service.enums.OpenDepositCheckerStatus;
//
//import java.util.*;
//
//@Component
//public class DepositCheckerServiceMock implements DepositCheckerService {
//
//    private final DepositService depositService;
//
//    @Autowired
//    public DepositCheckerServiceMock(DepositService depositService) {
//        this.depositService = depositService;
//    }
//
//    @Override
//    @SneakyThrows
//    public OpenDepositCheckerStatus checkAndOpen(UserDTO userDTO) {
//        final String login = LoginInfoService.data.get(userDTO.getUuid());
//        final CheckingInfo checkingInfo = UserCheckingRequestsInfo.result.get(login);
//        while (checkingInfo.getServiceStatus().equals(Status.WAITING)) {
//            Random random = new Random();
//            int check = random.nextInt(3) + 1;
//            System.out.println(check);
//            Thread.sleep(1000);
//
//            switch (check) {
//                case 1:
//                    checkingInfo.setServiceStatus(Status.CHECKING_FAILED);
//                    checkingInfo.setErrors(getRandomErrors());
//                    break;
//                case 2:
//                    checkingInfo.setServiceStatus(Status.SUCCESS);
//                    DepositRequestDTO depositRequestDTO = DepositRequestDTO.builder()
//                            .passportNumber(userDTO.getPassportNumber())
//                            .uuid(userDTO.getUuid())
//                            .build();
//                    depositService.checkAndOpen(depositRequestDTO);
//                    break;
//                case 3:
//                    checkingInfo.setServiceStatus(Status.WAITING);
//                    break;
//            }
//            System.out.println(UserCheckingRequestsInfo.result.get(login).getServiceStatus());
//        }
//        return null;
//    }
//
//    private List<MvdErrors> getRandomErrors() {
//        List<MvdErrors> mvdErrorsList = new ArrayList<>();
//        Random random = new Random();
//        if (random.nextBoolean()) {
//            mvdErrorsList.add(MvdErrors.PERSONAL_DATA_DOESNT_EXIST);
//            return mvdErrorsList;
//        }
//
//        if (random.nextBoolean()) {
//            mvdErrorsList.add(MvdErrors.TERRORIST_ERROR);
//            return mvdErrorsList;
//        }
//        mvdErrorsList.add(MvdErrors.TIME_OUT_ERROR);
//        return mvdErrorsList;
//    }
//}
