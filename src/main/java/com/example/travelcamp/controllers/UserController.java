package com.example.travelcamp.controllers;

import com.example.travelcamp.enumm.Role;
import com.example.travelcamp.models.User;
import com.example.travelcamp.repositories.UserRepository;
import com.example.travelcamp.security.UserAuthSecurity;
import com.example.travelcamp.services.UserService;
import com.example.travelcamp.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:3000/")
//@RestController
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserValidator userValidator;
    private final UserService userService;

    private final UserRepository userRepository;

    public UserController(UserValidator userValidator, UserService userService, UserRepository userRepository) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/index")
    public String userIndex(Model model){
        //получаем пользователя авторизации и отправляем его представлению
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        System.out.println(Role.ADMIN.name());
        return "user/indexUser";
    }


}
