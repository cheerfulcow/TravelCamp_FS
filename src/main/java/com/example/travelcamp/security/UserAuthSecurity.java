package com.example.travelcamp.security;

import com.example.travelcamp.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

//Для авторизации пользователя
public class UserAuthSecurity implements UserDetails {

    private final User user;

    public UserAuthSecurity(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }


    //Получаем роль пользователя;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }

    //Наследуем методы интерфейса; Указываем нужные значения
    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getLogin();
    }

    //Аккаунт действителен?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //Аккаунт заблокирован?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //Пароль действителен (срок действия не истёк)?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //Аккаунт активен?
    @Override
    public boolean isEnabled() {
        return true;
    }
}
