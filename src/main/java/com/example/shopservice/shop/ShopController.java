package com.example.shopservice.shop;

import com.example.shopservice.shop.Order.OrderStatus;
import com.example.shopservice.shop.Product.Product;
import com.example.shopservice.shop.Product.ProductNotFoundException;
import com.example.shopservice.shop.Product.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.shopservice.shop.Order.Order;
import com.example.shopservice.shop.Order.OrderRepo;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class ShopController {
    private ProductRepo productRepo = new ProductRepo();
    private final OrderRepo orderRepo;

    @PostMapping("/orders")
    public Order addOrder(@RequestBody List<String> productIds)
    {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new ProductNotFoundException("Product.Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                // anstatt: System.out.println("Product.Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                //                return null;
            }
            products.add(productToOrder.get());
        }
        //anpassen der Methode mit dem Bestellstatus und einem Zeitstempel
        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING, ZonedDateTime.now());

        return orderRepo.save(newOrder);
    }

    //Hinzufügen einer Methode, die die Bestellungen über streams nach ihrem Status filtert
    @GetMapping("/orders")
    public List<Order> findAll(@RequestParam OrderStatus status) {
        return orderRepo.findAllBy(status);
    }

    //Hinzufügen einer Methode, die die IDs abgleicht und bei gleicher ID den Status mithilfe der Lombok Annotation aktualisiert
    @PutMapping("/orders/{id}")
    public void updateOrder (@PathVariable String id, @RequestParam OrderStatus newStatus) {
        Order oldOrder = orderRepo.getOrderById(id);
        orderRepo.removeOrderById(id);
        Order newOrder = oldOrder.withStatus(newStatus);
        orderRepo.save(newOrder);
    }

}
