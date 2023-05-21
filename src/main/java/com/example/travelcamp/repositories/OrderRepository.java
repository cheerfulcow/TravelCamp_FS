package com.example.travelcamp.repositories;

import com.example.travelcamp.models.Order;
import com.example.travelcamp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser (User user);

    // Поиск заказа по последним 4 символам в номере заказа
    // lower = приводим в нижний регистр,
    //?1 - указывает, что это первый параметр нашего метода (String number);
    //Маски: '%?1' - означает, что number ищем в конце строки
    //NativeQuery = true включает методы с применением SQL
    @Query(value = "select * from orders where order_number like %?1", nativeQuery = true)
    Optional<List<Order>> findOrdersByPartOfNumber(String orderNumber);
}
