package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.dto.PersonalDataDTO;
import ru.interns.deposit.service.impl.PersonalDataService;


@RestController
@RequestMapping("/api/v1/user/")
public class PersonalDataController {
    private PersonalDataService personalDataService;


    @Autowired
    public PersonalDataController(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }

    @PostMapping("/add_data")
    public ResponseEntity<?> addData(@RequestBody PersonalDataDTO personalData) {
        return ResponseEntity.ok(personalDataService.add(personalData).getStatus());
    }
    @GetMapping("/get_data")
    public ResponseEntity<PersonalData> getPersonalData(){
        return ResponseEntity.ok(personalDataService.get());
    }
}
