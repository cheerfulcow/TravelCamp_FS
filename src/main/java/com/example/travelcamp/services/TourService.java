package com.example.travelcamp.services;

import com.example.travelcamp.models.tours.Tour;
import com.example.travelcamp.models.tours.TourType;
import com.example.travelcamp.repositories.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TourService {
    private final TourRepository tourRepository;

    public TourService(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
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
       tourRepository.save(tour);
    }

    //Удаление тура по ID
    @Transactional
    public void deleteTour(long id) {
        tourRepository.deleteById(id);
    }
}
