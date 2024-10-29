package mosbach.dhbw.de.products.data.api;

import java.time.LocalDate;
import java.util.List;

public interface OrderManager {

    void addOrder(List<CartItem> orderedItems, Double totalPrice, LocalDate pickupDate, String customerID);
    List<Order> getAllOrders();
    void setAllOrders(List<Order> orders);

}
