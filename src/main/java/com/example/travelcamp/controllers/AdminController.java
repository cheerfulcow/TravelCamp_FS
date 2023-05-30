package com.example.travelcamp.controllers;

import com.example.travelcamp.enumm.OrderStatus;
import com.example.travelcamp.enumm.Role;
import com.example.travelcamp.enumm.TourTypeEnum;
import com.example.travelcamp.models.Order;
import com.example.travelcamp.models.User;
import com.example.travelcamp.models.tours.Tour;
import com.example.travelcamp.models.tours.TourImage;
import com.example.travelcamp.security.UserAuthSecurity;
import com.example.travelcamp.services.ImageService;
import com.example.travelcamp.services.OrderService;
import com.example.travelcamp.services.TourService;
import com.example.travelcamp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Value("${upload.path}") //путь для загрузки, указан в app prop
    private String uploadPath;

    private final UserService userService;
    private final TourService tourService;
    private final ImageService imageService;
    private final OrderService orderService;

    public AdminController(UserService userService, TourService tourService, ImageService imageService, OrderService orderService) {
        this.userService = userService;
        this.tourService = tourService;
        this.imageService = imageService;
        this.orderService = orderService;
    }

    @GetMapping("/index")
    public String adminIndex(Model model) {
        //получаем пользователя авторизации и отправляем его представлению (для вывода роли пользователя)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("tours", tourService.findAllTours());
        model.addAttribute("tourTypes", TourTypeEnum.values());
        return "admin/indexAdmin";
    }

    //По ссылке admin/tours/add возвращаем представление, отправляем в него новый Tour и типы туров
    @GetMapping("/tours/add")
    public String toursAdd(Model model) {
        model.addAttribute("tour", new Tour());
        model.addAttribute("tourType", TourTypeEnum.values()); //отправляем перечень типов туров
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        return "admin/toursAdd";
    }

    //Добавление нового тура. Принимаем @ModelAttribute объект модели "tour", под хранение модели создаём экземпляр подели Tour,
    // проводим валидацию @Valid и в BindingResult кладём все ошибки валидации, далее поскольку наши инпуты формы,
    // куда мы загружаем фотографии не привязаны к форме, получаем от них данные при помощи @RequestParam,
    // также принимаем выбранный в селекте тип тура.
    @PostMapping("/tours/add")
    public String toursAdd(@ModelAttribute("tour") @Valid Tour tour, BindingResult bindingResult,
                           @RequestParam("file_one") MultipartFile file_one,
                           @RequestParam("file_two") MultipartFile file_two,
                           @RequestParam("file_three") MultipartFile file_three,
                           @RequestParam("file_four") MultipartFile file_four,
                           @RequestParam("file_five") MultipartFile file_five,
                           @RequestParam("tourType") String tourType, Model model) throws IOException {
        model.addAttribute("tourType", TourTypeEnum.values());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        if (bindingResult.hasErrors()) {
            model.addAttribute("tourType", TourTypeEnum.values());
            model.addAttribute("user", userAuthSecurity.getUser());
            return "admin/toursAdd";
        }
        //Если ошибок нет, то обрабатываем и загружаем файлы с файлИнпутов формы.
        List<MultipartFile> fileList = new ArrayList<>();
        Collections.addAll(fileList, file_one, file_two, file_three, file_four, file_five);
        //Если ни одной картинки не загрузили, то по умолчанию подгружаем лого
        if (isListEmpty(fileList)) {
            saveLogo(tour);
        } else { //Если загруженные картинки есть, то сохраняем их
            saveImageAndAddToTour(fileList, tour);
        }
        tourService.saveNewTour(tour, tourType);
        return "redirect:/admin/index";
    }


    //Редактирование тура
    @GetMapping("tours/edit/{id}")
    public String tourEdit(@PathVariable("id") long id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("tour", tourService.getTourById(id));
        model.addAttribute("tourType", TourTypeEnum.values());
        return "admin/tourEdit";
    }

    //Обрабатываем форму редактирования тура
    @PostMapping("tours/edit/{id}")
    public String tourEdit(@PathVariable("id") long id,
                           @ModelAttribute("tour") @Valid Tour tour, BindingResult bindingResult,
                           @RequestParam("file_one") MultipartFile file_one,
                           @RequestParam("file_two") MultipartFile file_two,
                           @RequestParam("file_three") MultipartFile file_three,
                           @RequestParam("file_four") MultipartFile file_four,
                           @RequestParam("file_five") MultipartFile file_five,
                           Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
            model.addAttribute("user", userAuthSecurity.getUser());
            model.addAttribute("tourType", TourTypeEnum.values());
            return "admin/tourEdit";
        }
        List<MultipartFile> fileList = new ArrayList<>(); //Лист с пришедшими файлами
        Collections.addAll(fileList, file_one, file_two, file_three, file_four, file_five);
        deleteImagesFromDirectory(tourService.getTourById(id).getTourImagesList()); //удаляем старые картинки из папки
        imageService.deleteTourImageListByTourId(id); //удаляем лист с фотками
        //Если ни одной картинки не загрузили, то по умолчанию подгружаем лого
        if (isListEmpty(fileList)) {
            saveLogo(tour);
        } else { //Если загруженные картинки есть, то сохраняем их
            saveImageAndAddToTour(fileList, tour);
        }
        tourService.updateTour(id, tour);
        return "redirect:/admin/tours/info/{id}";
    }

    //Подробная информация о выбранном туре
    @GetMapping("/tours/info/{id}")
    public String tourInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("tour", tourService.getTourById(id));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        return "/admin/tourDetailed";
    }

    //Удаление тура
    @GetMapping("/tours/delete/{id}")
    public String tourDelete(@PathVariable("id") long id) {
        tourService.deleteTour(id);
        return "redirect:/admin/index";
    }

    //Список пользователей
    @GetMapping("/users")
    public String usersList(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("role", Role.values());
        return "admin/users";
    }

    //Информация о конкретном пользователе
    @GetMapping("/users/info/{id}")
    public String userInfo(@PathVariable("id") long id ,Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("userAuth", userAuthSecurity.getUser());
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("role", Role.values());
        model.addAttribute("dateTimeFormat", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return "admin/userInfo";
    }

    //Для изменения статуса пользователя
    @PostMapping("/users/info/{id}")
    public String userInfoEdit(@PathVariable("id") int id, @ModelAttribute("role") String role) {
        User user = userService.findUserById(id);
        user.setRole(role);
        userService.updateUser(id, user);
        return "redirect:/admin/users/info/{id}";
    }

    //Отображение страницы со всеми заказами
    @GetMapping("/orders")
    public String showAllOrders(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("userAuth", userAuthSecurity.getUser());
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("status", OrderStatus.values());
        model.addAttribute("value_search", "");
        return "/admin/orders";
    }

    //Подробная информация о заказе и изменение его статуса
    @GetMapping("/orders/{id}")
    public String orderInfo(@PathVariable("id") int id, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("userAuth", userAuthSecurity.getUser());
        model.addAttribute("order", orderService.getOrderById(id));
        model.addAttribute("status", OrderStatus.values());
        return "/admin/orderInfo";
    }

    //Изменение статуса заказа на странице с заказами  public String userInfoEdit(@PathVariable("id") int id, @ModelAttribute("role") String role) {
    @PostMapping("/orders/{id}")
    public String ChangeOrderStatus(@RequestParam("status") String orderStatus,
                                    @PathVariable("id") int id)
    {
        Order order = orderService.getOrderById(id); //получаем объект заказа из БД
        order.setOrderStatus(orderStatus); //меняем статус на выбранный в селекте
        orderService.updateOrder(id, order); //обновляем данные заказа в БД
        System.out.println("Ордер статус: " + orderStatus);
        return "redirect:/admin/orders/{id}";
    }

    //Поиск заказов по последним символам
    @PostMapping("/orders/search")
    public String orderSearch(@RequestParam("search") String search, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("userAuth", userAuthSecurity.getUser());
        //кладем в модель все заказы
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("search_order", orderService.findOrdersByPartOfNumber(search));
        //Кладём в модель обратно полученные значения с формы для того, чтобы после отправки формы (произойдёт перезагрузка
        // страницы) отправить в инпут ранее введённое значение
        model.addAttribute("value_search", search);
        return "/admin/orders";
    }






    // --------------   Controller Private Methods   -------------- //
    private void saveImageAndAddToTour(List<MultipartFile> fileList, Tour tour) throws IOException {
        for (int i = 0; i < fileList.size(); i++) {
            if (!fileList.get(i).isEmpty()) {
                //Генерируем UUID для файла и создаем новое уникальное имя файла UUID + исходное название файла
                String uuidFile = UUID.randomUUID().toString();
                String resultFile = uuidFile + "." + fileList.get(i).getOriginalFilename();
                //Отправляем файл с новым сгенерированным наименованием в папку с загрузками (ссылка на которую
                // хранится в uploadPath и указана в app. prop.).
                fileList.get(i).transferTo(new File(uploadPath + "/" + resultFile));
                TourImage image = new TourImage(); //создаем объект модели Image (фото)
                image.setTour(tour); //к фотографии привязываем тур
                image.setFileName(resultFile); //присваиваем наименование файлу
                tour.addImageToTour(image); //добавляем фото в лист с фото, привязанный к этому продукту
            }
        }
    }

    private void saveLogo(Tour tour) {
        String uuidLogoName = UUID.randomUUID().toString() + ".siteLogo";
        File srcLogo = new File(uploadPath + "/siteLogo.png"); //тут находится лого
        File destSrcLogo = new File(uploadPath + "/" + uuidLogoName); //сюда его копируем
        try {
            FileCopyUtils.copy(srcLogo, destSrcLogo); //копируем и перемещаем лого в папку с загрузками
            TourImage image = new TourImage();
            image.setTour(tour); //к фотографии привязываем тур
            image.setFileName(uuidLogoName); //присваиваем наименование файлу
            tour.addImageToTour(image); //добавляем фото в лист с фото, привязанный к этому продукту
        } catch (IOException e) {
            System.out.println("Ошибка копирования логотипа");
            e.printStackTrace();
        }
    }

    private boolean isListEmpty(List<MultipartFile> fileList) {
        boolean listEmpty = true;
        for (MultipartFile file : fileList) { //непустые файлы кладём в лист
            if (!file.isEmpty()) {
                listEmpty = false;
            }
        }
        return listEmpty;
    }

    private void deleteImagesFromDirectory(List<TourImage> tourImageList) {
        List<TourImage> currentTourImageList = new ArrayList<>();
        currentTourImageList.addAll(tourImageList);
        for (int i = 0; i < currentTourImageList.size(); i++) { //удаляем из папки старые фотки
            File fileToDelete = new File(uploadPath + "/" + currentTourImageList.get(i).getFileName());
            fileToDelete.delete(); //удаляем старое фото из папки
        }
    }


}
