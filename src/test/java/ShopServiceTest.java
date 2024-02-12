import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        assertNull(actual);
    }

    @Test
    void filterOrderByStatus_whenStatusIsProcessing_thenReturnFilteredOrders() {
        //GIVEN
        List<Order> statusOrders = new ArrayList<>();
        statusOrders.add(new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING));
        statusOrders.add(new Order("2", List.of(new Product("2", "Birne")), OrderStatus.IN_DELIVERY));
        statusOrders.add(new Order("3", List.of(new Product("3", "Banane")), OrderStatus.COMPLETED));
        statusOrders.add(new Order("4", List.of(new Product("4", "Kiwi")), OrderStatus.PROCESSING));

        ShopService shopService = new ShopService();

        //WHEN
        List<Order> actual = shopService.filterOrderByStatus(statusOrders, OrderStatus.PROCESSING);

        //THEN
        Assertions.assertNotNull(actual);
        for (Order order : actual) {
            assertEquals(OrderStatus.PROCESSING, order.status());
        }
    }

}
