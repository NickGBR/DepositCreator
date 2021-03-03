package ru.interns.deposit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.db.repositoiry.PersonalDataRepository;
import ru.interns.deposit.dto.PersonalDataDTO;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.enums.PersonalDataAddingStatus;

import java.util.*;

@Component
public class PersonalDataService {
    private PersonalDataRepository personalDataRepository;
    private PersonalDataMapper personalDataMapper;
    private UserService userService;

    @Autowired
    public PersonalDataService(PersonalDataRepository personalDataRepository,
                               PersonalDataMapper personalDataMapper,
                               UserService userService) {
        this.personalDataRepository = personalDataRepository;
        this.personalDataMapper = personalDataMapper;
        this.userService = userService;
    }


    public PersonalDataAddingStatus add(PersonalDataDTO personalDataDTO) {
        final PersonalData personalData = personalDataMapper.toEntity(personalDataDTO);
        if (personalDataRepository.existsByForeignKey(userService
                .getCurrentUser()
                .getId())) {
            return PersonalDataAddingStatus.DATA_ALREADY_EXIST;
        } else {
            personalData.setForeignKey(userService.getCurrentUser().getId());
            personalDataRepository.save(personalData);
            return PersonalDataAddingStatus.ADDED_SUCCESSFULLY;
        }
    }

    public PersonalData get() {
        return personalDataRepository.getByForeignKey(userService
                .getCurrentUser()
                .getId()
        );
    }

    //Test
    public PersonalData getPersonalByForeignKey(Long foreignKey) {
        return personalDataRepository.getByForeignKey(foreignKey);
    }

    @Transactional
    public Boolean delete() {
        final PersonalData personalData = personalDataRepository
                .getByForeignKey(userService
                        .getCurrentUser()
                        .getId());
        System.out.println(personalData);
        if (personalData != null) {
            personalDataRepository.delete(personalData);
            return true;
        } else return false;
    }
}
