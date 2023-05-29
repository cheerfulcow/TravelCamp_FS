package com.example.travelcamp.controllers;

import com.example.travelcamp.enumm.TourTypeEnum;
import com.example.travelcamp.models.User;
import com.example.travelcamp.models.tours.Tour;
import com.example.travelcamp.services.TourService;
import com.example.travelcamp.services.UserService;
import com.example.travelcamp.util.UserValidator;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.example.travelcamp.enumm.Role;
import com.example.travelcamp.security.UserAuthSecurity;
import com.example.travelcamp.services.UserAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class HomeController {

    private final UserAuthService userAuthService;
    private final UserValidator userValidator;
    private final UserService userService;
    private final TourService tourService;

    public HomeController(UserAuthService userAuthService, UserValidator userValidator, UserService userService, TourService tourService) {
        this.userAuthService = userAuthService;
        this.userValidator = userValidator;
        this.userService = userService;
        this.tourService = tourService;
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
            return "redirect:/authentication";
        }
    }

    @GetMapping("/index")
    public String userIndex(Model model) {
        model.addAttribute("tours", tourService.findAllTours());
        model.addAttribute("tourTypes", TourTypeEnum.values());
        model.addAttribute("search_tour", new ArrayList<Tour>());
        model.addAttribute("isSearch", false);
        return "index";
    }

    @PostMapping("/index/search")
    public String tourSearch(Model model,
                             @RequestParam("search") String search,
                             @RequestParam("priceFrom") String priceFrom,
                             @RequestParam("priceUpTo") String priceUpTo,
                             @RequestParam(value = "sortByPrice", required = false, defaultValue = "") String sortByPrice,
                             @RequestParam(value = "tourType", required = false, defaultValue = "") String tourType
    ) {
        model.addAttribute("isSearch", true);
        model.addAttribute("tours", tourService.findAllTours());
        model.addAttribute("tourTypes", TourTypeEnum.values());
        if (!search.equals("")) {
            model.addAttribute("search_tour", tourService.getToursByTitle(search));
            return "index";
        }
        if (!priceFrom.isEmpty() || !priceUpTo.isEmpty()) { //Если задан диапазон цен
            if (!tourType.isEmpty()) {
                if (sortByPrice.equals("sorted_by_ascending_price")) { //диапазон цен + сортировка по возрастанию + тип тура
                    model.addAttribute("search_tour", tourService.getToursByPriceRangeAndTourTypeSortByPriceAsc(priceFrom, priceUpTo, tourType));
                } else if (sortByPrice.equals("sorted_by_descending_price")) { //диапазон цен + сортировка по убыванию цены + тип тура
                    model.addAttribute("search_tour", tourService.getToursFilterByPriceRangeAndTourTypeSortByPriceDesc(priceFrom, priceUpTo, tourType));
                } else
                    model.addAttribute("search_tour", tourService.getToursFilterByPriceRangeAndTourType(priceFrom, priceUpTo, tourType));
            } else {
                if (sortByPrice.equals("sorted_by_ascending_price")) { //диапазон цен + сортировка по возрастанию цены
                    model.addAttribute("search_tour", tourService.getToursFilterByPriceRangeSortByPriceAsc(priceFrom, priceUpTo)); //диапазон цен + сортировка по возрастанию цены
                } else if (sortByPrice.equals("sorted_by_descending_price")) { //диапазон цен + сортировка по убыванию цены
                    model.addAttribute("search_tour", tourService.getToursFilterByPriceRangeSortByPriceDesc(priceFrom, priceUpTo));
                } else {  //Если выбран только диапазон цен (любое из полей)
                    model.addAttribute("search_tour", tourService.getToursFilterByPriceRange(priceFrom, priceUpTo));
                }
            }
        } else { //Если диапазон цен не задан
            if (sortByPrice.equals("sorted_by_ascending_price")) {
                if (!tourType.isEmpty()) {
                    model.addAttribute("search_tour", tourService.getToursByTourTypeSortByPriceAsc(tourType));
                } else model.addAttribute("search_tour", tourService.getToursSortByPriceAsc());
            } else if (sortByPrice.equals("sorted_by_descending_price")) {
                if (!tourType.isEmpty()) {
                    model.addAttribute("search_tour", tourService.getToursByTourTypeSortByPriceDesc(tourType));
                } else model.addAttribute("search_tour", tourService.getToursSortByPriceDesc());
            } else model.addAttribute("search_tour", tourService.getToursByTourType(tourType));
        }
        model.addAttribute("priceFrom", priceFrom);
        model.addAttribute("priceUpTo", priceUpTo);
        return "index";
    }

    //Метод для получения информации о конкретном туре
    @GetMapping("/tours/info/{id}")
    public String infoProduct(@PathVariable("id") long id, Model model) {
        model.addAttribute("tour", tourService.getTourById(id));
        return "tourDetailed";
    }
}




