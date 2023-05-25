package com.example.travelcamp.services;

import com.example.travelcamp.models.tours.Tour;
import com.example.travelcamp.models.tours.TourImage;
import com.example.travelcamp.repositories.ImageRepository;
import com.example.travelcamp.repositories.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TourService {
    private final TourRepository tourRepository;

    private final ImageRepository imageRepository;

    public TourService(TourRepository tourRepository, ImageRepository imageRepository) {
        this.tourRepository = tourRepository;
        this.imageRepository = imageRepository;
    }

    public List<Tour> findAllTours () {
        return tourRepository.findAll();
    }

    @Transactional
    public void saveNewTour(Tour tour, String  tourType){
        tour.setTourType(tourType);
        tourRepository.save(tour);
    }

    //Получаем тур по ID
    public Tour getTourById(long id) {
       return tourRepository.findById(id).orElse(null);
    }

    //Метод позволяет обновить данные о товаре
    //Т.к. мы передаём ID, spring Data JPA сам понимает, что мы хотим обновить информацию о продукте
    @Transactional
    public void updateTour(long id, Tour tour) {
       tour.setId(id);
       tour.setDateTime(LocalDateTime.now());
       tourRepository.save(tour);
    }

    //Удаление тура по ID
    @Transactional
    public void deleteTour(long id) {
        tourRepository.deleteById(id);
    }
}
