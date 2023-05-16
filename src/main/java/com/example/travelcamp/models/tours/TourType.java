package com.example.travelcamp.models.tours;

import jakarta.persistence.*;

import java.util.List;

// --- Класс для реализации типа туров через таблицу БД. Пока отключён, т.к. пока используем Enum --- //

//@Entity
public class TourType {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    private String title;
//
//    //Храним список туров, принадлежащих определенному типу (вело/пеше и тд)
//    //mappedBy - указываем с каким полем класса Tour устанавливаем связь
//    //fetch = FetchType.EAGER = при загрузке владеемого объекта необходимо сразу загрузить коллекцию владельцев
//    @OneToMany (mappedBy = "tourType", fetch = FetchType.EAGER)
//    private List<Tour> tours;
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public List<Tour> getTours() {
//        return tours;
//    }
//
//    public void setTours(List<Tour> tours) {
//        this.tours = tours;
//    }
}
