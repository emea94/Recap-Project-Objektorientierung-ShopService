import Order.Order;
import Product.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");
        ZonedDateTime fixedTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());

        //WHEN
        Order actual = shopService.addOrder(productsIds);

        //THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, fixedTime);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectException() {
        //GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        //THEN -> WHEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));

    }

    @Test
    void filterOrderByStatus_whenStatusIsProcessing_thenReturnFilteredOrders() {
        //GIVEN
        List<Order> statusOrders = new ArrayList<>();
        ZonedDateTime fixedTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
        statusOrders.add(new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, fixedTime));
        statusOrders.add(new Order("2", List.of(new Product("2", "Birne")), OrderStatus.IN_DELIVERY, fixedTime));
        statusOrders.add(new Order("3", List.of(new Product("3", "Banane")), OrderStatus.COMPLETED, fixedTime));
        statusOrders.add(new Order("4", List.of(new Product("4", "Kiwi")), OrderStatus.PROCESSING, fixedTime));

        ShopService shopService = new ShopService();

        //WHEN
        List<Order> actual = shopService.filterOrderByStatus(statusOrders, OrderStatus.PROCESSING);

        //THEN
        Assertions.assertNotNull(actual);
        for (Order order : actual) {
            assertEquals(OrderStatus.PROCESSING, order.status());
        }
    }

    @Test
    void updateOrder_whenGivenIDequalsOrderID_thenUpdateOrderStatus() {
        //GIVEN
        ZonedDateTime fixedTime = ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
        Order initialOrder = new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, fixedTime);;
        String id = "1";
        OrderStatus newStatus = OrderStatus.IN_DELIVERY;
        //WHEN
        Order updatedOrder = ShopService.updateOrder(initialOrder, id, newStatus);
        //THEN
        assertEquals(id, updatedOrder.id());
        assertEquals(newStatus, updatedOrder.status());
        assertNotEquals(initialOrder, updatedOrder);
    }

}
