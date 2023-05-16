package com.example.travelcamp.util;

import com.example.travelcamp.models.User;
import com.example.travelcamp.services.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    public final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userService.findByLogin(user) != null) {
            errors.rejectValue("login", "", "Этот логин уже занят");
        }
        if (userService.findByEmail(user) != null) {
            errors.rejectValue("email", "", "Email адрес уже занят");
        }
    }
}
