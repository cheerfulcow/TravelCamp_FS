package com.example.travelcamp.controllers;

import com.example.travelcamp.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    @GetMapping("/index")
    public String index(@ModelAttribute("user") User user) {
        return "moderator/indexModerator";
    }
}
