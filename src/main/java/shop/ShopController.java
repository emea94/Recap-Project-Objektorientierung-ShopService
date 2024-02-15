package shop;

import org.springframework.web.bind.annotation.*;
import shop.Order.Order;
import shop.Order.OrderRepo;
import shop.Order.OrderStatus;
import shop.Product.Product;
import shop.Product.ProductNotFoundException;
import shop.Product.ProductRepo;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/shop")
public class ShopController {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo;

    public ShopController(OrderRepo orderRepo) {
        this.orderRepo;
    }

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
        return orderRepo.findAllByy(status);
    }

    //Hinzufügen einer Methode, die die IDs abgleicht und bei gleicher ID den Status mithilfe der Lombok Annotation aktualisiert
    @PutMapping("/orders/{id}")
    public void updateOrder (@PathVariable String id, @RequestParam OrderStatus newStatus) {
        Order oldOrder = orderRepo.getOrderById(id);
        orderRepo.removeOrder(id);
        Order newOrder = oldOrder.withStatus(newStatus);
        orderRepo.save(newOrder);
    }

}
