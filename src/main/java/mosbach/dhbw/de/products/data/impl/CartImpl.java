package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.Cart;
import mosbach.dhbw.de.products.data.api.CartItem;

import java.util.List;

public class CartImpl implements Cart {

    private List<CartItem> cartItems;
    private String cartID;
    private String customerID;

    public CartImpl(List<CartItem> cartItems, String cartID, String customerID) {
        this.cartItems = cartItems;
        this.cartID = cartID;
        this.customerID = customerID;
    }

    @Override
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    @Override
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}
