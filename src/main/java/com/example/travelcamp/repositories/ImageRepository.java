package com.example.travelcamp.repositories;

import com.example.travelcamp.models.tours.TourImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<TourImage, Long> {
}
