package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.dto.ResponseDTO;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.DepositResponseDTO;
import ru.interns.deposit.external.mvd.dto.CheckingInfo;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.DepositCheckerService;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.Status;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/v1/user/deposit/")
public class DepositController {


    private final DepositCheckerService checkerService;
    private final PersonalDataMapper mapper;
    private final PersonalDataService personalDataService;
    private final UserService userService;

    private final DepositService depositService;

    @Autowired
    public DepositController(@Qualifier("depositCheckerServiceImpl")DepositCheckerService checkerService, PersonalDataMapper mapper,
                             PersonalDataService personalDataService, UserService userService,
                             DepositService depositService) {
        this.checkerService = checkerService;
        this.mapper = mapper;
        this.personalDataService = personalDataService;
        this.userService = userService;
        this.depositService = depositService;
    }

    @GetMapping("/open")
    public ResponseEntity<?> openDeposit() {
        // Mock для открытия депозит сервиса без проверок
        // depositService.open(mapper.toUserDto(personalDataService.get()));
        Map<Services, Status> servicesInfo = new HashMap<>();
        servicesInfo.put(Services.CHEATER, Status.WAITING);
        servicesInfo.put(Services.MVD, Status.WAITING);
        final List<Errors> errors = new ArrayList<>();

        UserCheckingRequestsInfo.result.put(userService.getCurrentUser().getLogin(),
                CheckingInfo.builder()
                        .errors(errors)
                        .serviceStatus(servicesInfo)
                        .build());

        final UUID uuid = UUID.randomUUID();
        // Добавляем в бд, пару логин uuid для получения логина пользователя по uuid
        LoginInfoService.data.put(uuid, userService.getCurrentUser().getLogin());

        final UserDTO userDTO = mapper.toUserDto(personalDataService.get());
        userDTO.setUuid(uuid);
        checkerService.checkAndOpen(userDTO);
        return ResponseEntity.ok("deposit/open");
    }

    @GetMapping("/get")
    public ResponseEntity<DepositResponseDTO> getDeposits(Long passportNumber) {

        return ResponseEntity.ok(depositService.getDeposits(personalDataService.get().getPassportNumber()));
    }

    @GetMapping("/check_status")
    // ОСТАНОВИЛСЯ ТУТ
    public ResponseEntity<ResponseDTO> checkOpeningStatus(){
        return ResponseEntity.ok(UserCheckingRequestsInfo.result
                .get(userService.getCurrentUser().getLogin()).toResponseDTO());
    }
}
