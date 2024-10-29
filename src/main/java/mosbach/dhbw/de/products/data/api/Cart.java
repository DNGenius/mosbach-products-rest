package mosbach.dhbw.de.products.data.api;

import java.util.List;

public interface Cart {

    List<CartItem> getCartItems();
    void setCartItems(List<CartItem> cartItems);
    String getCartID();
    void setCartID(String cartID);
    String getCustomerID();
    void setCustomerID(String customerID);

}
