package com.example.travelcamp.controllers;

import com.example.travelcamp.repositories.UserRepository;
import com.example.travelcamp.security.UserAuthSecurity;
import com.example.travelcamp.services.TourService;
import com.example.travelcamp.services.UserService;
import com.example.travelcamp.util.UserValidator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:3000/")
//@RestController
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserValidator userValidator;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TourService tourService;

    public UserController(UserValidator userValidator, UserService userService, UserRepository userRepository, TourService tourService) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.userRepository = userRepository;
        this.tourService = tourService;
    }

    @GetMapping("/index")
    public String userIndex(Model model){
        //получаем пользователя авторизации и отправляем его представлению
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("tours", tourService.findAllTours());
        return "user/indexUser";
    }


}
