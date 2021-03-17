package ru.interns.deposit.controller;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.binary.BinaryObject;
import org.apache.ignite.binary.BinaryObjectBuilder;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.binary.BinaryObjectImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import ru.interns.deposit.db.temprorary.UserCheckingRequestsInfo;
import ru.interns.deposit.db.temprorary.LoginInfoService;
import ru.interns.deposit.dto.ApacheIgniteDTO;
import ru.interns.deposit.dto.ResponseDTO;
import ru.interns.deposit.dto.UserDTO;
import ru.interns.deposit.enums.Services;
import ru.interns.deposit.external.deposit.DepositService;
import ru.interns.deposit.external.deposit.dto.DepositResponseDTO;
import ru.interns.deposit.external.enums.Status;
//import ru.interns.deposit.external.mvd.dto.CheckingInfo;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.DepositCheckerService;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;
import ru.interns.deposit.util.Cache;

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
    private Ignite ignite;
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
        //IgniteCache<UUID, ClientDTO> accounts = ignite.cache("my-cache2");
        // Mock для открытия депозит сервиса без проверок
        // depositService.open(mapper.toUserDto(personalDataService.get()));
        Map<Services, Status> servicesInfo = new HashMap<>();
        servicesInfo.put(Services.CHEATER, Status.SUCCESS);
        servicesInfo.put(Services.MVD, Status.WAITING);
        final List<Errors> errors = new ArrayList<>();

/*        UserCheckingRequestsInfo.result.put(userService.getCurrentUser().getLogin(),
                CheckingInfo.builder()
                        .errors(errors)
                        .serviceStatus(servicesInfo)
                        .build());*/

        final UUID uuid = UUID.randomUUID();
        ApacheIgniteDTO apacheIgniteDTO = ApacheIgniteDTO.builder()
                .serviceStatus(servicesInfo)
                .uuid(uuid)
                .errors(errors)
                .build();

        //accounts.put(uuid,clientDTO);
        //System.out.println(accounts.get(uuid));
        //Cache.cache.put(userService.getCurrentUser().getLogin(),clientDTO);
        BinaryObjectImpl builder = Ignition.ignite().binary().toBinary(apacheIgniteDTO);

        //builder.setField("name", "John");
        //BinaryObject binaryObj = builder.;
        //person = builder.build();
/*        IgniteCache<String, ApacheIgniteDTO> cache =
                ignite.getOrCreateCache(new CacheConfiguration<String, ApacheIgniteDTO>("my-cache")
                        .setCacheMode(CacheMode.LOCAL));*/
        ignite.cache("my-cache2").put(userService.getCurrentUser().getLogin(), apacheIgniteDTO);
        System.out.println(ignite.cache("my-cache2").get(userService.getCurrentUser().getLogin()));
        ApacheIgniteDTO apacheIgniteDTO1 = (ApacheIgniteDTO) ignite.cache("my-cache2").get(userService.getCurrentUser().getLogin());
        System.out.println(apacheIgniteDTO1+ "ессссссссссссссссссссссссссссссссссссс");
        //System.out.println(ignite.cache("my-cache2").get(userService.getCurrentUser().getLogin()));
        // Добавляем в бд, пару логин uuid для получения логина пользователя по uuid
/*        LoginInfoService.data.put(uuid, userService.getCurrentUser().getLogin());

        final UserDTO userDTO = mapper.toUserDto(personalDataService.get());
        userDTO.setUuid(uuid);
        checkerService.checkAndOpen(userDTO);*/
        return ResponseEntity.ok("deposit/open");
    }

    @GetMapping("/get")
    public ResponseEntity<DepositResponseDTO> getDeposits(Long passportNumber) {

        return ResponseEntity.ok(depositService.getDeposits(personalDataService.get().getPassportNumber()));
    }

    @GetMapping("/check_status")
    // ОСТАНОВИЛСЯ ТУТ
    public ResponseEntity<ResponseDTO> checkOpeningStatus(){
        ApacheIgniteDTO userDTO = null; //Cache.getUser(userService.getCurrentUser().getLogin());
/*        return ResponseEntity.ok(UserCheckingRequestsInfo.result
                .get(userService.getCurrentUser().getLogin()).toResponseDTO());*/
        return ResponseEntity.ok(ResponseDTO.builder().build());
    }
}
