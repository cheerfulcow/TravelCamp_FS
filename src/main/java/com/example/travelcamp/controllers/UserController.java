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

    // При отправке формы спринг положит объект в модель. Если в модели такого объекта не будет, спринг в эту модель объект положит.
    // При гет-запросе на /registration спринг смотрит, приходит какой-либо объект в качестве запросу по данному адресу,
    // если нет объекта, то спринг автоматически внедрит аттрибут User
    @GetMapping("/registration")
    public String registration (@ModelAttribute("user") User user) {
        return "registration";
    }

    @PostMapping("/registration")
    public String resultRegistration (@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        //Дополнительная валидация на наличие регистрируемого логина и емейл в БД (в дополнение к валидации, указанной в модели Person).
        // Если пользователь уже существует, валидатор внедрит ошибку в bindingResult
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        else {
            userService.registrationNewUser(user);
            return "redirect:index";
        }
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

    @GetMapping("")
    public String getUsers(){
        System.out.println("отдал пользователей");
//        return userRepository.findAll();
        return "index";
    }

    //индус section
//    @PostMapping("/save")
//    public String saveUser(@RequestBody UserDTO userDTO) {
//        String id = userService.addUser(userDTO);
//        return id;
//    }

//    @CrossOrigin
//    @PostMapping("/registration")
//    public ResponseEntity<?> registration(@RequestBody User newUser, BindingResult bindingResult) {
//        System.out.println("пришли данные регистрации");
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
//        } else {
//            User createdUser = userService.createNewUser(newUser);
//            return ResponseEntity.ok(createdUser);
//        }
//    }



}
