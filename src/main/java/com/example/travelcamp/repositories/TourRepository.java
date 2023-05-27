package com.example.travelcamp.repositories;

import com.example.travelcamp.models.tours.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    //Поиск туров по наименованию(по подстроке(части) наименования), игнорируя регистр
    List<Tour> findTourByTitleContainingIgnoreCase(String title);

    List<Tour> findTourByTourType(String tourType);

    //Сортировка по убыванию цены
    List<Tour> findAllByOrderByPriceSmallGroupDesc();

    //Сортировка по возрастанию цены
    List<Tour> findAllByOrderByPriceSmallGroupAsc();

    //Поиск по ценовому диапазону
    @Query(value = "select * from tour where (price_small_group >= ?1 and price_small_group <= ?2)", nativeQuery = true)
    List<Tour> findTourByPriceRange(double priceFrom, double priceUpTo);

    //Поиск по ценовому диапазону и сортировка по возрастанию цены
    @Query(value = "select * from tour where (price_small_group >= ?1 and price_small_group <= ?2) order by price_small_group asc ", nativeQuery = true)
    List<Tour> findTourByPriceRangeSortByPriceAsc(double priceFrom, double priceUpTo);

    //Поиск по ценовому диапазону и сортировка по убыванию цены
    @Query(value = "select * from tour where (price_small_group >= ?1 and price_small_group <= ?2) order by price_small_group desc ", nativeQuery = true)
    List<Tour> findTourByPriceRangeSortByPriceDesc(double priceFrom, double priceUpTo);

    //Поиск по ценовому диапазону и типу тура
    @Query(value = "select * from tour where (price_small_group >= ?1 and price_small_group <= ?2) and (tour_type LIKE ?3)", nativeQuery = true)
    List<Tour> findTourByPriceRangeAndTourType (double priceFrom, double priceUpTo, String tourType);

    //Поиск по ценовому диапазону и типу тура, сортировка по возрастанию цены
    @Query(value = "select * from tour where (price_small_group >= ?1 and price_small_group <= ?2) and (tour_type LIKE ?3) order by price_small_group asc", nativeQuery = true)
    List<Tour> findTourByPriceRangeAndTourTypeSortByPriceAsc (double priceFrom, double priceUpTo, String tourType);

    //Поиск по ценовому диапазону и типу тура, сортировка по убыванию цены
    @Query(value = "select * from tour where (price_small_group >= ?1 and price_small_group <= ?2) and (tour_type LIKE ?3) order by price_small_group desc", nativeQuery = true)
    List<Tour> findTourByPriceRangeAndTourTypeSortByPriceDesc (double priceFrom, double priceUpTo, String tourType);

    //Сортировка по типу тура и по убыванию цены
    @Query(value = "select * from tour where (tour_type LIKE ?1) order by price_small_group desc", nativeQuery = true)
    List<Tour> findTourByTourTypeSortByPriceDesc (String tourType);

    //Сортировка по типу тура и по возрастанию цены
    @Query(value = "select * from tour where (tour_type LIKE ?1) order by price_small_group asc", nativeQuery = true)
    List<Tour> findTourByTourTypeSortByPriceAsc (String tourType);


}
