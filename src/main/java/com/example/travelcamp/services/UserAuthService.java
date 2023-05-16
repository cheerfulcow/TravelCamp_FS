package com.example.travelcamp.services;

import com.example.travelcamp.models.User;
import com.example.travelcamp.repositories.UserRepository;
import com.example.travelcamp.security.UserAuthSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//Сервис для авторизации пользователей
@Service
public class UserAuthService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserAuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //наследуем метод
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Получаем пользователя из таблицы по логину с формы аутентификации
        Optional<User> user = userRepository.findByLogin(username);
        //Если пользователь не был найден
        if (user.isEmpty()) {
    //выбрасываем исключение, что пользователь не найден.
    // Данное исключение будет поймано SS и сообщение будет выведено на страницу
            throw new UsernameNotFoundException("Неверный логин");
        }
        //Если найден, то создаем новый объект UserAuthSecurity
        return new UserAuthSecurity(user.get());
    }
}
