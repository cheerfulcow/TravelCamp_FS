package com.example.travelcamp.controllers;

import com.example.travelcamp.enumm.TourTypeEnum;
import com.example.travelcamp.models.tours.Tour;
import com.example.travelcamp.models.tours.TourImage;
import com.example.travelcamp.security.UserAuthSecurity;
import com.example.travelcamp.services.TourService;
import com.example.travelcamp.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    private TourImage tourImage;

    public AdminController(UserService userService, TourService tourService) {
        this.userService = userService;
        this.tourService = tourService;
    }

    @GetMapping("/index")
    public String adminIndex(Model model) {
        //получаем пользователя авторизации и отправляем его представлению (для вывода роли пользователя)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserAuthSecurity userAuthSecurity = (UserAuthSecurity) authentication.getPrincipal();
        model.addAttribute("user", userAuthSecurity.getUser());
        model.addAttribute("tours", tourService.findAllTours());
        return "admin/indexAdmin";
    }

    //По ссылке admin/tours/add возвращаем представление, отправляем в него новый Tour и типы туров
    @GetMapping("/tours/add")
    public String toursAdd(Model model) {
        model.addAttribute("tour", new Tour());
        model.addAttribute("tourType", TourTypeEnum.values()); //отправляем перечень типов туров
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
        if (bindingResult.hasErrors()) {
            model.addAttribute("tourType", TourTypeEnum.values());
            return "admin/toursAdd";
        }
        //Если ошибок нет, то обрабатываем и загружаем файлы с файлИнпутов формы.
        List<MultipartFile> fileList = new ArrayList<>();
        Collections.addAll(fileList, file_one, file_two, file_three, file_four, file_five);
        for(int i = 0; i < fileList.size(); i++) {
            if(!fileList.get(i).isEmpty()){
                saveImageAndAddToTour(fileList.get(i), tour);
            }
        }
        tourService.saveNewTour(tour, tourType);
        return "redirect:/admin/index";
    }

    //Редактирование тура
    @GetMapping("tours/edit/{id}")
    public String tourEdit(@PathVariable("id") long id, Model model) {
        model.addAttribute("tour", tourService.getTourById(id));
        model.addAttribute("tourType", TourTypeEnum.values());
        return "/admin/tourEdit";
    }

    //Обрабатываем форму редактирования тура
    @PostMapping("tours/edit/{id}")
    public String tourEdit(@PathVariable("id") long id,
                           @ModelAttribute("tour") @Valid Tour tour,
                           @RequestParam("file_one") MultipartFile file_one,
                           @RequestParam("file_two") MultipartFile file_two,
                           @RequestParam("file_three") MultipartFile file_three,
                           @RequestParam("file_four") MultipartFile file_four,
                           @RequestParam("file_five") MultipartFile file_five,
                           BindingResult bindingResult,
                           Model model) throws IOException {
        if (bindingResult.hasErrors()) {
            model.addAttribute("tour", tourService.getTourById(id));
            model.addAttribute("tourType", TourTypeEnum.values());
            return "/admin/tourEdit";
        }
        List<MultipartFile> fileList = new ArrayList<>();
        Collections.addAll(fileList, file_one, file_two, file_three, file_four, file_five);
        for(int i = 0; i < fileList.size(); i++) {
            if(!fileList.get(i).isEmpty()){
                saveImageAndAddToTour(fileList.get(i), tour);
            }
        }
        tourService.updateTour(id, tour);
        return "/admin/indexAdmin";
    }












    public void saveImageAndAddToTour(MultipartFile fileName, Tour tour) throws IOException {
        //Генерируем UUID для файла и создаем новое уникальное имя файла UUID + исходное название файла
        String uuidFile = UUID.randomUUID().toString();
        String resultFile = uuidFile + "." + fileName.getOriginalFilename();
        //Отправляем файл с новым сгенерированным наименованием в папку с загрузками (ссылка на которую
        // хранится в uploadPath и указана в app. prop.).
        fileName.transferTo(new File(uploadPath + "/" + resultFile));
        TourImage image = new TourImage(); //создаем объект модели Image (фото)
        image.setTour(tour); //к фотографии привязываем тур
        image.setFileName(resultFile); //присваиваем наименование файлу
        tour.addImageToTour(image); //добавляем фото в лист с фото, привязанный к этому продукту
    }
}