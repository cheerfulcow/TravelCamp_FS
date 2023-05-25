package com.example.travelcamp.repositories;

import com.example.travelcamp.models.Cart;
import com.example.travelcamp.models.tours.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    //Список туров в корзине пользователя
    List<Cart> findToursByUserId(long id);

    //Ищем конкретный тур в корзине пользователя
    Optional<Cart> findByUserIdAndTourId(long user_id, long tour_id);

    //Удаление тура из корзины по id тура
    @Modifying
    @Query(value = "delete from tour_cart where (user_id=?1 and tour_id=?2)", nativeQuery = true)
    void deleteTourFromCartByTourId(long user_id , long tour_id);

    //Удаление всех туров из корзины
    @Modifying
    void deleteAllByUserId(long user_id);
}
