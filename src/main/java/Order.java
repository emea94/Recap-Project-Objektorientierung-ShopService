import java.util.List;

public record Order(
        String id,
        List<Product> products,
        //Hinzufügen der Bestellstatus_Variablen
        OrderStatus status
) {
}
