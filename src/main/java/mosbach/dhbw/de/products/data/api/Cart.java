package mosbach.dhbw.de.products.data.api;

import java.util.List;

public interface Cart {

    List<CartItem> getCartItems();
    String getCartID();
    String getCustomerID();

}
