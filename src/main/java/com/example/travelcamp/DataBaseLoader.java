package com.example.travelcamp;

import com.example.travelcamp.enumm.Role;
import com.example.travelcamp.models.User;
import com.example.travelcamp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;


import java.io.File;

//При запуске будет создан админ
@Component
@Controller
public class DataBaseLoader implements CommandLineRunner {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public DataBaseLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${upload.path}") //путь для загрузки, указан в app prop
    private String uploadPath;

    //Создаём папку для хранения загрузочных файлов, если отсутствует
    public void mkDirUploadPath() {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }

    //Добавляем ADMIN, если его ещё нет в БД
    public void addAdmin() {
        String loginAdmin = "admin";
        String roleAdmin = Role.ADMIN.name();
        String emailAdmin = "TravelCamp@yandex.ru";
        String passwordAdmin = "admin";
        String passwordAdminEncoded = passwordEncoder.encode(passwordAdmin);
        System.out.println(passwordAdminEncoded);
        String phoneAdmin = "+70000000000";
        String firstnameAdmin = "Админ";
        String secondnameAdmin = "Админович";
        if (userRepository.findByRole(roleAdmin).isEmpty()) {
            this.userRepository.save(new User(loginAdmin, passwordAdminEncoded, roleAdmin, emailAdmin, phoneAdmin, firstnameAdmin, secondnameAdmin));
        }
    }

    @Override
    public void run(String... args) throws Exception {
        addAdmin();
        mkDirUploadPath();
    }
}
