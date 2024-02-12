import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShopService {
    private ProductRepo productRepo = new ProductRepo();
    private OrderRepo orderRepo = new OrderMapRepo();

    public Order addOrder(List<String> productIds)  //Bei Checked-Exception: throws ProductNotFoundException
    {
        List<Product> products = new ArrayList<>();
        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            if (productToOrder.isEmpty()) {
                throw new ProductNotFoundException("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                // anstatt: System.out.println("Product mit der Id: " + productId + " konnte nicht bestellt werden!");
                //                return null;
            }
            products.add(productToOrder.get());
        }
        //anpassen der Methode mit dem Bestellstatus
        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }

    //Hinzufügen einer Methode, die die Bestellungen über streams nach ihrem Status filtert
    public List<Order> filterOrderByStatus(List<Order> orders, OrderStatus status) {
        return orders.stream()
                .filter(order -> order.status().equals(status))
                .collect(Collectors.toList());

    }

}
