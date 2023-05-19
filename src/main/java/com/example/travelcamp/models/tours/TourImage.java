package com.example.travelcamp.models.tours;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;


@Entity
public class TourImage {

    @Value("${upload.path}") //путь для загрузки, указан в app prop
    private String uploadPath;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fileName;

    //Связываем картинку с определенным туром
    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    private Tour tour;

    public TourImage(long id, String fileName, Tour tour) {
        this.id = id;
        this.fileName = fileName;
        this.tour = tour;
    }

     public TourImage() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String title) {
        this.fileName = title;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }
}
