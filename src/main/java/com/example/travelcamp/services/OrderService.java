package com.example.travelcamp.services;

import com.example.travelcamp.models.Order;
import com.example.travelcamp.models.User;
import com.example.travelcamp.repositories.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    //Получаем список заказов с сортировкой по времени заказа, начиная с последнего
    public List<Order> getAllOrders(){
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "dateTime"));
    }
    public List<Order> getOrderByUser(User user){
       return orderRepository.findByUser(user);
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
