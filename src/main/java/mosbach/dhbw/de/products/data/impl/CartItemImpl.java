package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.CartItem;
import mosbach.dhbw.de.products.data.api.Product;

public class CartItemImpl implements CartItem {

    private Product cartProduct;
    private Integer quantity;

    public CartItemImpl(Product cartProduct, Integer quantity) {
        this.cartProduct = cartProduct;
        this.quantity = quantity;
    }

    @Override
    public Product getCartProduct() {
        return cartProduct;
    }

    public void setCartProduct(Product cartProduct) {
        this.cartProduct = cartProduct;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
