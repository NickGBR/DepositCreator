package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.dto.PersonalDataDTO;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.impl.PersonalDataService;


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
    public ResponseEntity<?> addData(@RequestBody PersonalDataDTO personalData) {
        return ResponseEntity.ok(personalDataService.add(personalData).getStatus());
    }
    @GetMapping("/get")
    public ResponseEntity<PersonalDataDTO> getPersonalData(){
        return ResponseEntity.ok(mapper
                .toDto(personalDataService.get())
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deletePersonalData(){
        return ResponseEntity.ok(personalDataService.delete());
    }
}
