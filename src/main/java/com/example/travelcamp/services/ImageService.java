package com.example.travelcamp.services;

import com.example.travelcamp.repositories.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ImageService {

    public final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void deleteTourImageListByTourId(long id) {
        imageRepository.deleteTourImageListByTourId(id);
    }
}
