package mosbach.dhbw.de.products.data.api;

import mosbach.dhbw.de.products.model.CustomerCart;

import java.time.LocalDate;
import java.util.List;

public interface Order {

    List<CartItem> getOrderedItems();
    Double getTotalPrice();
    LocalDate getPickupDate();
    String getCustomerID();

}
