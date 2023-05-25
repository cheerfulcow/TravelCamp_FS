package com.example.travelcamp.services;

import com.example.travelcamp.models.Cart;
import com.example.travelcamp.models.User;
import com.example.travelcamp.repositories.CartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getAllToursFromCart (long user_id) {
       return cartRepository.findToursByUserId(user_id);
    }

    public Cart getTourFromCart(long user_id, long tour_id){
        Optional<Cart> tourFromCart = cartRepository.findByUserIdAndTourId(user_id, tour_id);
        return tourFromCart.orElse(null);
    }

    public void addTourInCart(Cart cart){
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteTourFromCart(long user_id , long tour_id) {
        cartRepository.deleteTourFromCartByTourId(user_id , tour_id);
    }
}
