package com.example.travelcamp.services;

import com.example.travelcamp.models.tours.Tour;
import com.example.travelcamp.models.tours.TourImage;
import com.example.travelcamp.repositories.ImageRepository;
import com.example.travelcamp.repositories.TourRepository;
import org.springframework.data.jpa.repository.Query;
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


//    ------- search methods --------     //

    //Поиск туров по наименованию(по подстроке(части) наименования), игнорируя регистр
    public List<Tour> getToursByTitle(String title){
       return tourRepository.findTourByTitleContainingIgnoreCase(title);
    }

    //Поиск по типу тура
    public List<Tour> getToursByTourType(String tourType) {
        return tourRepository.findTourByTourType(tourType);
    }

    //Сортировка по возрастанию цены
    public List<Tour> getToursSortByPriceAsc(){
        return tourRepository.findAllByOrderByPriceSmallGroupAsc();
    }

    //Сортировка по убыванию цены
    public List<Tour> getToursSortByPriceDesc(){
        return tourRepository.findAllByOrderByPriceSmallGroupDesc();
    }

    //Поиск по ценовому диапазону
    public List<Tour> getToursFilterByPriceRange(String priceFrom, String priceUpTo) {
        return tourRepository.findTourByPriceRange(Double.parseDouble(setPriceFromIfEmpty(priceFrom)),
                Double.parseDouble(setPriceUpToIfEmpty(priceUpTo)));
    }

    //Поиск по ценовому диапазону и сортировка по убыванию цены
    public List<Tour> getToursFilterByPriceRangeSortByPriceDesc(String priceFrom, String priceUpTo) {
        return tourRepository.findTourByPriceRangeSortByPriceDesc(Double.parseDouble(setPriceFromIfEmpty(priceFrom)),
                Double.parseDouble(setPriceFromIfEmpty(setPriceUpToIfEmpty(priceUpTo))));
    }

    //Поиск по ценовому диапазону и сортировка по возрастанию цены
    public List<Tour> getToursFilterByPriceRangeSortByPriceAsc(String priceFrom, String priceUpTo) {
        return tourRepository.findTourByPriceRangeSortByPriceAsc(Double.parseDouble(setPriceFromIfEmpty(priceFrom)),
                Double.parseDouble(setPriceUpToIfEmpty(priceUpTo)));
    }

    //Поиск по ценовому диапазону и типу тура
    public List<Tour> getToursFilterByPriceRangeAndTourType (String priceFrom, String priceUpTo, String tourType) {
        return tourRepository.findTourByPriceRangeAndTourType(Double.parseDouble(setPriceFromIfEmpty(priceFrom)),
                Double.parseDouble(setPriceUpToIfEmpty(priceUpTo)), tourType);
    }

    //Поиск по ценовому диапазону и типу тура, сортировка по возрастанию цены
    public List<Tour> getToursByPriceRangeAndTourTypeSortByPriceAsc (String priceFrom, String priceUpTo, String tourType) {
        return tourRepository.findTourByPriceRangeAndTourTypeSortByPriceAsc(Double.parseDouble(setPriceFromIfEmpty(priceFrom)),
                Double.parseDouble(setPriceUpToIfEmpty(priceUpTo)), tourType);
    }

    //Поиск по ценовому диапазону и типу тура, сортировка по убыванию цены
    public List<Tour> getToursFilterByPriceRangeAndTourTypeSortByPriceDesc (String priceFrom, String priceUpTo, String tourType) {
        return tourRepository.findTourByPriceRangeAndTourTypeSortByPriceDesc(Double.parseDouble(setPriceFromIfEmpty(priceFrom)),
                Double.parseDouble(setPriceUpToIfEmpty(priceUpTo)), tourType);
    }

    //Сортировка по типу тура и по убыванию цены
    public List<Tour> getToursByTourTypeSortByPriceDesc (String tourType) {
        return tourRepository.findTourByTourTypeSortByPriceDesc(tourType);
    }

    //Сортировка по типу тура и по возрастанию цены
    public List<Tour> getToursByTourTypeSortByPriceAsc (String tourType) {
        return tourRepository.findTourByTourTypeSortByPriceAsc(tourType);
    }



//    ------ private methods area ------    //
    private String setPriceFromIfEmpty(String priceFrom) {
        if (priceFrom.isEmpty()) {
            priceFrom = "0";
            return priceFrom;
        } else return priceFrom;
    }

    private String setPriceUpToIfEmpty(String priceUpTo) {
        if (priceUpTo.isEmpty()) {
            priceUpTo = "10000000";
            return priceUpTo;
        } else return priceUpTo;
    }


}
