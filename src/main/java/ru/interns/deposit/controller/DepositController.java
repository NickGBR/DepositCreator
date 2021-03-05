package ru.interns.deposit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.interns.deposit.db.temprorary.MvdStatus;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.DepositRequestDTO;
import ru.interns.deposit.external.deposit.dto.DepositResponseDTO;
import ru.interns.deposit.external.enums.CheckingStatus;
import ru.interns.deposit.external.mvd.dto.MvdResultCheckingDTO;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.DepositCheckerService;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user/deposit/")
public class DepositController {


    private final DepositCheckerService checkerService;
    private final PersonalDataMapper mapper;
    private final PersonalDataService personalDataService;
    private final UserService userService;

    private final DepositService depositService;

    public DepositController(DepositCheckerService checkerService, PersonalDataMapper mapper,
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
        //depositService.open(mapper.toUserDto(personalDataService.get()));
        MvdStatus.mvdCheckResult.put(userService.getCurrentUser().getLogin(),
                MvdResultCheckingDTO.builder()
                        .checkingStatus(CheckingStatus.WAITING)
                        .build());
        final UUID uuid = UUID.randomUUID();
        // Добавляем в бд, пару логин uuid для получения логина пользователя по uuid
        LoginInfoService.data.put(uuid, userService.getCurrentUser().getLogin());

        final UserDTO userDTO = mapper.toUserDto(personalDataService.get());
        userDTO.setUuid(uuid);

        //checkerService.checkAndOpen(userDTO);
        //Mock
        depositService.checkAndOpen(DepositRequestDTO.builder().passportNumber(userDTO.getPassportNumber())
        .uuid(userDTO.getUuid()).build());
        return ResponseEntity.ok("deposit/open");
    }

    @GetMapping("/get")
    public ResponseEntity<DepositResponseDTO> getDeposits(Long passportNumber) {

        return ResponseEntity.ok(depositService.getDeposits(personalDataService.get().getPassportNumber()));
    }

    @GetMapping("/check_status")
    // ОСТАНОВИЛСЯ ТУТ
    public ResponseEntity<MvdResultCheckingDTO> checkOpeningStatus(){
        return ResponseEntity.ok(MvdStatus.mvdCheckResult
                .get(userService.getCurrentUser().getLogin()));
    }
}
