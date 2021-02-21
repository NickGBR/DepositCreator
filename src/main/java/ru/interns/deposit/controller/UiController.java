package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.interns.deposit.db.repositoiry.DepositRepository;
import ru.interns.deposit.security.RegistrationDTO;
import ru.interns.deposit.mapper.UserMapper;

@Controller
@RequestMapping("app/")
public class UiController {

    private UserMapper mapper;
    private DepositRepository repository;

    @Autowired
    public UiController(UserMapper mapper, DepositRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }


    @PostMapping("/create")
    public @ResponseBody Boolean create(@RequestBody RegistrationDTO registrationDTO){
        System.out.println(registrationDTO);
        //final Deposit deposit = mapper.toEntity(userDTO);
        //repository.save(deposit);
        return true;
    }
}
