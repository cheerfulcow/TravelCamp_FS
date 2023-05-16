package com.example.travelcamp.services;

import com.example.travelcamp.enumm.Role;
import com.example.travelcamp.models.User;
import com.example.travelcamp.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrationNewUser (User user) {
        //хэшируем пароль, пришедший с формы
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER.name()); //по умолчанию регистрируем как user
        userRepository.save(user);
    }

    public User findByLogin (User user) {
        Optional<User> userDB = userRepository.findByLogin(user.getLogin());
        return userDB.orElse(null);
    }

    public User findByEmail (User user) {
        Optional<User> userDB = userRepository.findByEmail(user.getEmail());
        return userDB.orElse(null);
    }

    public User createNewUser (User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER.name());
        return userRepository.save(user);
    }
}
