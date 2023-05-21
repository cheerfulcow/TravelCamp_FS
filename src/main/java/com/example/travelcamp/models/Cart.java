package com.example.travelcamp.models;

import jakarta.persistence.*;

@Entity
@Table(name="tour_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //id пользователя, которому принадлежит данная корзина
    @Column(name="user_id")
    private long userId;

    @Column(name="tour_id")
    private long tourId;

    public Cart(long userId, long tourId) {
        this.userId = userId;
        this.tourId = tourId;
    }

    public Cart() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTourId() {
        return tourId;
    }

    public void setTourId(long tourId) {
        this.tourId = tourId;
    }
}
