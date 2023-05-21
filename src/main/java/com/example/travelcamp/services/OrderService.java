package com.example.travelcamp.services;

import com.example.travelcamp.models.Order;
import com.example.travelcamp.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    public final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public Order getOrderById(long id){
        Optional<Order> optionalOrder=orderRepository.findById(id);
        return optionalOrder.orElse(null);
    }

    //Метод позволяет обновить данные о заказе(статус)
    //Т.к. мы передаём ID, spring Data JPA сам понимает, что мы хотим обновить информацию о объекте
    @Transactional
    public void updateOrder (long id, Order order) {
        order.setId(id);
        orderRepository.save(order);
    }

    public List<Order> findOrdersByPartOfNumber(String orderNumber){
        Optional<List<Order>> ordersSearchResult = orderRepository.findOrdersByPartOfNumber(orderNumber);
        return ordersSearchResult.orElse(null);
    }

}
