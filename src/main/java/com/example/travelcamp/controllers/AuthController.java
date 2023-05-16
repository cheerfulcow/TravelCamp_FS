package com.example.travelcamp.controllers;

import com.example.travelcamp.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/authentication")
    public String login() {
        return "authentication";
    }
}
