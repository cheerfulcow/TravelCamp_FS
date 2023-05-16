package com.example.travelcamp.models;

import com.example.travelcamp.enumm.OrderStatus;
import com.example.travelcamp.models.tours.Tour;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table (name = "orders") //order - зарезервированное posgreSQL слово, применять нельзя - даст ошибку
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String orderNumber;

    private String orderStatus;

    private LocalDateTime dateTime;

    //Количество заказе
    private int count;

    private double totalCost;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Tour tours;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @PrePersist
    private void init(){
        dateTime = LocalDateTime.now();
    }

    public Order(String orderNumber, String orderStatus, LocalDateTime dateTime, int count, double totalCost, Tour tours, User user) {
        this.orderNumber = orderNumber;
        this.orderStatus = orderStatus;
        this.dateTime = dateTime;
        this.count = count;
        this.totalCost = totalCost;
        this.tours = tours;
        this.user = user;
    }

    public Order() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Tour getTours() {
        return tours;
    }

    public void setTours(Tour tours) {
        this.tours = tours;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
