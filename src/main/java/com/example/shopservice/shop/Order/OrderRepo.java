package com.example.shopservice.shop.Order;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderRepo extends MongoRepository<Order, String> {

    List<Order> findAllBy(OrderStatus status);

    Order getOrderById(String id);

    void removeOrderById(String id);
}
