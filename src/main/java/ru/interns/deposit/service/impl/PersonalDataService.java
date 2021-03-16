package ru.interns.deposit.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.interns.deposit.db.dao.PersonalData;
import ru.interns.deposit.db.repositoiry.PersonalDataRepository;
import ru.interns.deposit.dto.PersonalDataDTO;
import ru.interns.deposit.mapper.PersonalDataMapper;
import ru.interns.deposit.service.enums.Errors;

import java.util.*;

@Component
public class PersonalDataService {
    private PersonalDataRepository personalDataRepository;
    private PersonalDataMapper personalDataMapper;
    private UserService userService;
    private ValidationService validationService;

    @Autowired
    public PersonalDataService(PersonalDataRepository personalDataRepository, PersonalDataMapper personalDataMapper,
                               UserService userService, ValidationService validationService) {
        this.personalDataRepository = personalDataRepository;
        this.personalDataMapper = personalDataMapper;
        this.userService = userService;
        this.validationService = validationService;
    }

    public List<Errors> add(PersonalDataDTO personalDataDTO) {
        final List<Errors> errors = new ArrayList<>();
        final PersonalData personalData = personalDataMapper.toEntity(personalDataDTO);

        System.out.println(personalData.getName());

        if (personalDataRepository.existsByForeignKey(userService
                .getCurrentUser()
                .getId())) {
            errors.add(Errors.PERSONAL_DATA_ALREADY_EXIST);
            return errors;
        } if (personalDataRepository.existsByPassportNumber(personalDataDTO.getPassportNumber())){
            errors.add(Errors.PASSPORT_ALREADY_EXIST);
        }

        validationService.validateDateOfBirthday(personalData.getDateOfBirthday(), errors);
        validationService.validatePassport(personalData.getPassportNumber(), errors);
        validationService.validateUserName(personalData.getName(), errors);
        validationService.validateUserSurname(personalData.getSurname(), errors);

        if (errors.size() == 0){
            personalData.setForeignKey(userService.getCurrentUser().getId());
            personalDataRepository.save(personalData);
            return null;
        }
        else {
            return errors;
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
