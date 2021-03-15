package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.interns.deposit.dto.PersonalDataDTO;
import ru.interns.deposit.dto.ResponseDTO;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.enums.Errors;
import ru.interns.deposit.service.enums.Status;
import ru.interns.deposit.service.impl.PersonalDataService;

import java.util.*;


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
        final List<Errors> errors = personalDataService.add(personalData);
        if (errors == null)
            return ResponseEntity.ok(ResponseDTO.builder().status(Status.SUCCESS.getStatus()).build());
        else return ResponseEntity.ok(ResponseDTO.builder().status(Status.CHECKING_FAILED.getStatus())
                .errors(errors)
                .build());
    }

    @GetMapping("/get")
    public ResponseEntity<PersonalDataDTO> getPersonalData() {
        return ResponseEntity.ok(mapper
                .toDto(personalDataService.get())
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deletePersonalData() {
        return ResponseEntity.ok(personalDataService.delete());
    }
}
