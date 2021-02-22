package ru.interns.deposit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/api/view/")
public class ViewResolverController {
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(){
        return "registration";
    }

    @GetMapping("/success")
    public String getSuccess(){
        return "authorized";
    }

    @GetMapping("/error")
    public String getError(){
        return "not_authorized";
    }
}
