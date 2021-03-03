package ru.interns.deposit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.interns.deposit.dto.RegistrationDTO;

@RestController
@RequestMapping("api/v1/")
public class UiController {

    @PostMapping("user/register")
    public @ResponseBody Boolean create(@RequestBody RegistrationDTO registrationDTO){
        //final Deposit deposit = mapper.toEntity(userDTO);
        //repository.save(deposit);
        return true;
    }
}
