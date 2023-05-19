package com.example.travelcamp.models;

import com.example.travelcamp.enumm.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class User {

    @Column
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;

    @Column(name = "login", nullable = false, columnDefinition = "text",unique = true)
    @NotEmpty(message = "это поле обязательно для заполнения")
    @Size(min = 5, max = 50, message = "Логин должен быть не короче 5 и не длиннее 50 символов")
    private String login;

    @Column(name = "password", nullable = false)
    @NotEmpty(message = "это поле обязательно для заполнения")
    @Size(min = 5, max = 150, message = "Пароль должен быть содержать от 5 до 150 символов")
    //паттерн закрыт, т.к. принимаем encoded пароль.
//    @Pattern(regexp = "([A-Za-z0-9]{5,50})$", message = "Используйте латинский алфавит(верхний, нижний регистр) и цифры")
    private String password;

    @Column(name = "role", nullable = false)
    private String  role;

    @Column (nullable = false, unique = true)
    @NotEmpty(message = "это поле обязательно для заполнения")
    @Email(message = "Вы ввели некорректный e-mail")
    private String email;

    @Column
    @NotEmpty(message = "это поле обязательно для заполнения")
    @Pattern(regexp = "^((\\+7|7|8)+([0-9]){10})$", message = "введите номер телефона, начиная с (+7 или 7 или 8), без пробелов и дефисов. Например: +79005554433")
    private String phoneNumber;

    @Column
    @NotEmpty(message = "это поле обязательно для заполнения")
    @Size(min = 2, max = 50, message = "имя не должно быть короче 2 и длиннее 50 символов")
    @Pattern(regexp = "([A-Za-zА-Яа-яЁё\\-]{2,30})$", message = "введите имя(латиница, кириллица)")
    private String firstName;

    @Column
    @NotEmpty(message = "это поле обязательно для заполнения")
    @Size(min = 2, max = 50, message = "фамилия не должна быть короче 2 и длиннее 50 символов")
    @Pattern(regexp = "([A-Za-zА-Яа-яЁё\\-]{2,30})$", message = "введите фамилию(латиница, кириллица)")
    private String secondName;

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
    private List<Order> orderList;

    //КОРЗИНА

    private LocalDateTime dateTime;

    public User() {
    }

    public User(String login, String password, String role, String email, String phoneNumber, String firstName, String secondName) {
        this.login = login;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    //Автоматическая запись времени создания пользователя
    @PrePersist
    private void userInit() {
        dateTime = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return id == user.id && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(role, user.role) && Objects.equals(email, user.email) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(dateTime, user.dateTime);
    }

    //Данный метод преобразует объект класса в число. Обратно преобразовать практически невозможно
    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, email, phoneNumber, dateTime);
    }
}
