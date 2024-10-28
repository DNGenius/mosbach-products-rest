package mosbach.dhbw.de.products.data.api;

public interface CartItem {

    Product getCartProduct();
    void setCartProduct(Product cartProduct);
    Integer getQuantity();
    void setQuantity(Integer quantity);

}
