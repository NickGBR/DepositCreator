package ru.interns.deposit.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.interns.deposit.dto.PersonalDataDTO;
import ru.interns.deposit.dto.ResponseDTO;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.Status;
import ru.interns.deposit.service.impl.PersonalDataService;
import ru.interns.deposit.service.impl.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/personal_data/")
public class PersonalDataController {
    private final PersonalDataService personalDataService;
    private final PersonalDataMapper mapper;

    @Autowired
    public PersonalDataController(PersonalDataService personalDataService, PersonalDataMapper mapper) {
        this.personalDataService = personalDataService;
        this.mapper = mapper;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseDTO> addData(@RequestBody PersonalDataDTO personalData) {
        log.info("Получен зарпос на добавление персональных данных от пользователя");
        final List<Errors> errors = personalDataService.add(personalData);
        if (errors == null) {
            log.info("Персональные данные успешно добавлены");
            return ResponseEntity.ok(ResponseDTO.builder().status(Status.SUCCESS.getStatus()).build());
        }
        log.warn("Произошла ошибка при добалении персональных данных");
        return ResponseEntity.ok(ResponseDTO.builder().status(Status.CHECKING_FAILED.getStatus())
                .errors(errors)
                .build());
    }

    @GetMapping("/get")
    public ResponseEntity<PersonalDataDTO> getPersonalData() {
        log.info("Получен зарпос на получение персональных данных от пользователя");
        return ResponseEntity.ok(mapper
                .toDto(personalDataService.get())
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deletePersonalData() {
        log.info("Получен зарпос на удаление персональных данных");
        return ResponseEntity.ok(personalDataService.delete());
    }
}
