package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.interns.deposit.db.repositoiry.DepositRepository;
import ru.interns.deposit.dto.RegistrationDTO;
import ru.interns.deposit.mapper.UserMapper;

@RestController
@RequestMapping("api/v1/")
public class UiController {

    private UserMapper mapper;
    private DepositRepository repository;

    @Autowired
    public UiController(UserMapper mapper, DepositRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    @PostMapping("user/register")
    public @ResponseBody Boolean create(@RequestBody RegistrationDTO registrationDTO){
        //final Deposit deposit = mapper.toEntity(userDTO);
        //repository.save(deposit);
        return true;
    }
}
