package com.example.travelcamp.controllers;

import com.example.travelcamp.enumm.OrderStatus;
import com.example.travelcamp.enumm.TourTypeEnum;
import com.example.travelcamp.models.Cart;
import com.example.travelcamp.models.Order;
import com.example.travelcamp.models.tours.Tour;
import com.example.travelcamp.models.tours.TourType;
import com.example.travelcamp.repositories.UserRepository;
import com.example.travelcamp.security.UserAuthSecurity;
import com.example.travelcamp.services.CartService;
import com.example.travelcamp.services.OrderService;
import com.example.travelcamp.services.TourService;
import com.example.travelcamp.services.UserService;
import com.example.travelcamp.util.UserValidator;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//@CrossOrigin(origins = "http://localhost:3000/")
//@RestController
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserValidator userValidator;
    private final UserService userService;
    private final UserRepository userRepository;
    private final TourService tourService;
    private final CartService cartService;
    private final OrderService orderService;

    public UserController(UserValidator userValidator, UserService userService, UserRepository userRepository, TourService tourService, CartService cartService, OrderService orderService) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.userRepository = userRepository;
        this.tourService = tourService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/index")
    public String userIndex(Model model) {
        //получаем пользователя авторизации и отправляем его представлению
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("tours", tourService.findAllTours());
        model.addAttribute("cart", cartService.getAllToursFromCart(userAuthSecurity.getUser().getId()));
        model.addAttribute("search_tour", new ArrayList<Tour>());
        model.addAttribute("isSearch", false);
        return "user/indexUser";
    }

    @PostMapping("/index/search")
    public String tourSearch(Model model,
                             @RequestParam("search") String search,
                             @RequestParam("priceFrom") String priceFrom,
                             @RequestParam("priceUpTo") String priceUpTo,
                             @RequestParam(value = "sortByPrice", required = false, defaultValue = "") String sortByPrice,
                             @RequestParam(value = "tourType", required = false, defaultValue = "") String tourType
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("isSearch", true);
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("tours", tourService.findAllTours());
        model.addAttribute("tourTypes", TourTypeEnum.values());
        model.addAttribute("cart", cartService.getAllToursFromCart(userAuthSecurity.getUser().getId()));
        if (!search.equals("")) {
            model.addAttribute("search_tour", tourService.getToursByTitle(search));
            return "user/indexUser";
        }
        if (!priceFrom.isEmpty() || !priceUpTo.isEmpty()) {
            if (!tourType.isEmpty()) {
                if (sortByPrice.equals("sorted_by_ascending_price")) { //диапазон цен + сортировка по возрастанию + тип тура
                    model.addAttribute("search_tour", tourService.getToursByPriceRangeAndTourTypeSortByPriceAsc(priceFrom, priceUpTo, tourType)); //диапазон цен + сортировка по возрастанию цены
                }
                if (sortByPrice.equals("sorted_by_descending_price")) { //диапазон цен + сортировка по убыванию цены + тип тура
                    model.addAttribute("search_tour", tourService.getToursFilterByPriceRangeAndTourTypeSortByPriceDesc(priceFrom, priceUpTo, tourType));
                }
                model.addAttribute("search_tour", tourService.getToursFilterByPriceRangeAndTourType(priceFrom, priceUpTo, tourType));
            }
            if (sortByPrice.equals("sorted_by_ascending_price")) { //диапазон цен + сортировка по возрастанию цены
                model.addAttribute("search_tour", tourService.getToursFilterByPriceRangeSortByPriceAsc(priceFrom, priceUpTo)); //диапазон цен + сортировка по возрастанию цены
            } else if (sortByPrice.equals("sorted_by_descending_price")) { //диапазон цен + сортировка по убыванию цены
                model.addAttribute("search_tour", tourService.getToursFilterByPriceRangeSortByPriceDesc(priceFrom, priceUpTo));
            }
            else {  //Если выбран только диапазон цен (любое из полей)
                model.addAttribute("search_tour", tourService.getToursFilterByPriceRange(priceFrom, priceUpTo));
            }
        }
        else {
            if (sortByPrice.equals("sorted_by_ascending_price")) {
                model.addAttribute("search_tour", tourService.getToursSortByPriceAsc());
            }
            if (sortByPrice.equals("sorted_by_descending_price")) {
                model.addAttribute("search_tour", tourService.getToursSortByPriceDesc());
            }
        }


        model.addAttribute("search_tour", new ArrayList<Tour>());
        return "user/indexUser";
    }

    //Метод для получения информации о конкретном туре
    @GetMapping("/tours/info/{id}")
    public String infoProduct(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("tour", tourService.getTourById(id));
        model.addAttribute("cart", cartService.getAllToursFromCart(userAuthSecurity.getUser().getId()));
        return "user/tourDetailed";
    }

    //Обрабатываем ссылку "добавить в корзину" из представления indexUser
    @GetMapping("/cart/add/{id}")
    public String addTourInCart(@PathVariable("id") long tour_id) {
        //Получаем продукт по id
        Tour tour = tourService.getTourById(tour_id);
        long user_id = getAuthUserId();
        Cart cart = new Cart(user_id, tour.getId()); //создаем объект корзины (id пользователя и id продукта)
        cartService.addTourInCart(cart); //добавляем в корзину
        return "redirect:/user/index#ourTours";
    }

    //Обрабатываем ссылку "удалить товар из корзины" из представления Index
    @GetMapping("/cart/delete/{id}")
    public String deleteTourFromCart(@PathVariable("id") long tour_id) {
        long user_id = getAuthUserId();
        cartService.deleteTourFromCart(user_id, tour_id); //удаляем тур из корзины
        return "redirect:/user/index";
    }

    //Обрабатываем ссылку "удалить товар из корзины" из корзины
    @GetMapping("/cart/deletefromcart/{id}")
    public String deleteTourFromCartInCartTemplate(@PathVariable("id") long tour_id) {
        long user_id = getAuthUserId();
        cartService.deleteTourFromCart(user_id, tour_id); //удаляем тур из корзины
        return "redirect:/user/cart";
    }

    //Корзина
    @GetMapping("/cart")
    public String cart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Извлекаем объект аутентифицированного пользователя
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal(); //вытаскиваем из объекта аутентификации объект модели person
        long user_id = userAuthSecurity.getUser().getId(); //извлекаем id пользователя из объекта
        List<Tour> tourList = getTourListFromCart(user_id);
        //Вычисление итоговой цены товаров в корзине
        float price = 0;
        for (Tour tour : tourList) {
            price += tour.getPriceSmallGroup();
        }
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("price", price);
        model.addAttribute("cartTours", tourList);
        return "/user/cart";
    }

    //Обрабатываем нажатие на ссылку "оформить заказ"
    @PostMapping("/order/create")
    public String order(@RequestParam("personNumber") int personNumber,
                        @RequestParam("tourDate") List<String> tourDateList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Извлекаем объект аутентифицированного пользователя
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal(); //вытаскиваем из объекта аутентификации объект модели person
        long user_id = userAuthSecurity.getUser().getId(); //извлекаем id пользователя из объекта
        List<Tour> tourList = getTourListFromCart(user_id); //получаем список туров из корзины пользователя
        //Проверяем инпут даты, чтобы он был не ранее +2 дня от текущей даты, иначе возвращаем форму с ошибкой
//        LocalDate tourDate = LocalDate.parse(tourDateString);
//        if(tourDate.isBefore(LocalDate.now().plusDays(2))){
//            bindingResult.rejectValue("tourDate", "invalid.date", "Выбранная дата должна быть не ранее " + LocalDate.now().plusDays(2));
//            model.addAttribute("user", userAuthSecurity.getUser());
//            model.addAttribute("cartTours", tourList);
//            model.addAttribute("tourDate", tourDateString);
//            return "/user/cart";
//    }
        createOrder(tourList, tourDateList, personNumber, userAuthSecurity);
        return "redirect:/user/orders";
    }

    @GetMapping("/orders")
    public String orderUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Извлекаем объект аутентифицированного пользователя
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        //получаем список всех заказов для конкретного пользователя
        List<Order> orderList = orderService.getOrderByUser(userAuthSecurity.getUser());
        model.addAttribute("orders", orderList);
        model.addAttribute("user", userAuthSecurity.getUser());
        return "/user/orders";
    }


// ------------------- private methods section ---------------- //

    private long getAuthUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Извлекаем объект аутентифицированного пользователя
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal(); //вытаскиваем из объекта аутентификации объект модели person
        long user_id = userAuthSecurity.getUser().getId(); //извлекаем id пользователя из объекта
        return user_id;
    }

    private List<Tour> getTourListFromCart(long user_id) {
        List<Cart> cartList = cartService.getAllToursFromCart(user_id);
        List<Tour> tourList = new ArrayList<>();
        //формируем список туров из корзины
        for (Cart cart : cartList) {
            tourList.add(tourService.getTourById(cart.getTourId()));
        }
        return tourList;
    }

    //Создание заказа
    private void createOrder(List<Tour> tourList, List<String> tourDate, int personNumber, UserAuthSecurity userAuthSecurity) {
        for (int i = 0; i < tourList.size(); i++) {
            //формируем уникальный номер заказа
            String orderNumber = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "-" + UUID.randomUUID().toString();
            //Для каждого из товара в корзине отдельный заказ
            Order newOrder = new Order(orderNumber, OrderStatus.ПРИНЯТ.name(), tourDate.get(i), personNumber, 1, tourList.get(i).getPriceSmallGroup(), tourList.get(i), userAuthSecurity.getUser());
            orderService.saveOrder(newOrder);
            cartService.deleteTourFromCart(userAuthSecurity.getUser().getId(), tourList.get(i).getId()); //удаляем заказанный товар из корзины
        }
    }

}