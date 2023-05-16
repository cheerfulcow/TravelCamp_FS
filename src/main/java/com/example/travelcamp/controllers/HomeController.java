package com.example.travelcamp.controllers;

import com.example.travelcamp.services.TourService;
import org.springframework.ui.Model;
import com.example.travelcamp.enumm.Role;
import com.example.travelcamp.security.UserAuthSecurity;
import com.example.travelcamp.services.UserAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final UserAuthService userAuthService;

    private final TourService tourService;



    public HomeController(UserAuthService userAuthService, TourService tourService) {
        this.userAuthService = userAuthService;
        this.tourService = tourService;
    }


    @GetMapping("/index")
    public String nonAuthPersonIndex(){
        return "index";
    }

    @GetMapping("/person_account")
    public String AuthPersonIndex (Model model) {
    // Получаем объект аутентификации. Далее, с помощью SpringContextHolder обращаемся к контексту и на нем вызываем метод аутентификации.
    // Из сессии текущего пользователя получаем объект, который был положен в данную сессию после аутентификации пользователя
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
    //получаем роль аутентифицированного пользователя от полученного объекта сессии
        String role = userAuthSecurity.getUser().getRole();
        model.addAttribute("tours", tourService.findAllTours());
        if (role.equals(Role.ADMIN.name())) {
            return "redirect:/admin/index";
        }
        if (role.equals(Role.MODERATOR.name())) {
            return "redirect:/moderator/index";
        }
        if (role.equals(Role.USER.name())) {
            model.addAttribute("user", userAuthSecurity.getUser());
            return "redirect:/user/index";
        }
        return "index";
    }
}




