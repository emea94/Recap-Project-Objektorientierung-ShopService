package Order;

import lombok.With;

import java.time.ZonedDateTime;
import java.util.List;

public record Order(
        String id,
        List<Product> products,
        //Hinzufügen der Bestellstatus_Variablen
        @With OrderStatus status,
        ZonedDateTime orderDate
) {
}
