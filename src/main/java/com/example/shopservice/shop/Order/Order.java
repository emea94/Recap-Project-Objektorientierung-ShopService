package com.example.shopservice.shop.Order;

import com.example.shopservice.shop.Product.Product;
import lombok.With;

import java.time.ZonedDateTime;
import java.util.List;

@With
public record Order(
        String id,
        List<Product> products,
        //Hinzufügen der Bestellstatus_Variablen
        OrderStatus status,
        ZonedDateTime orderDate
) {
}
