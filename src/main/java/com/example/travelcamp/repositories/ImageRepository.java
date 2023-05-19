package com.example.travelcamp.repositories;

import com.example.travelcamp.models.tours.TourImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<TourImage, Long> {

    //Удаление всего списка картинок у тура по id.
    @Modifying
    @Query(value = "delete from tour_image where (tour_id = ?1)", nativeQuery = true)
    void deleteTourImageListByTourId(long id);
}
