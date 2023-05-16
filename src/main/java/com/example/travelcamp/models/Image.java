//package com.example.travelcamp.models;
//
//import com.example.travelcamp.models.tours.Tour;
//import jakarta.persistence.*;
//
//@Entity
//public class Image {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String fileName;
//
//    @ManyToOne(fetch = FetchType.EAGER, optional = false)
//    private Tour tour;
//
//    public Image(String fileName, Tour tour) {
//        this.fileName = fileName;
//        this.tour = tour;
//    }
//
//    public Image() {
//    }
//
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public String getFileName() {
//        return fileName;
//    }
//
//    public void setFileName(String fileName) {
//        this.fileName = fileName;
//    }
//
//    public Tour getTour() {
//        return tour;
//    }
//
//    public void setTour(Tour tour) {
//        this.tour = tour;
//    }
//}
