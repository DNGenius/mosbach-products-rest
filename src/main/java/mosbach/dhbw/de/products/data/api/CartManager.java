package mosbach.dhbw.de.products.data.api;

import java.util.List;

public interface CartManager {

    void addProductToCart(String customerID, Product selectedProduct, Integer quantity);
    boolean removeProductFromCart(String customerID, String productID);
    List<Cart> getAllCarts();
    void setAllCarts(List<Cart> carts);
    Cart getCartByCustomerID(String customerID);
    Double getTotalPriceByCustomerID(String customerID);

}
