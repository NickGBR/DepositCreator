package ru.interns.deposit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/view/")
public class ViewResolverController {
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(){
        return "registration";
    }

    @GetMapping("/main_page")
    public String getSuccess(){
        return "main_page";
    }

    @GetMapping("/error")
    public String getError(){
        return "not_authorized";
    }
}
