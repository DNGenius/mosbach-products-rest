package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.CartItem;
import mosbach.dhbw.de.products.data.api.Order;
import mosbach.dhbw.de.products.model.CustomerCart;

import java.time.LocalDate;
import java.util.List;

public class OrderImpl implements Order {

    private List<CartItem> orderedItems;
    private Double totalPrice;
    private LocalDate pickupDate;
    private String customerID;

    public OrderImpl (List<CartItem> orderedItems, Double totalPrice, LocalDate pickupDate, String customerID) {
        this.orderedItems = orderedItems;
        this.totalPrice = totalPrice;
        this.pickupDate = pickupDate;
        this.customerID = customerID;
    }

    @Override
    public List<CartItem> getOrderedItems() {
        return orderedItems;
    }

    public void setOrderedItems(List<CartItem> orderedItems) {
        this.orderedItems = orderedItems;
    }

    @Override
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }

    @Override
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}
