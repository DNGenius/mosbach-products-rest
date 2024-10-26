package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.Product;

public class ProductImpl implements Product {

    private String displayName;
    private String category;
    private Integer weightInGrams;
    private Integer quantity;
    private Double priceInEuro;
    private String productID;

    public ProductImpl(String displayName, String category, Integer weightInGrams, Integer quantity, Double priceInEuro, String productID) {
        this.displayName = displayName;
        this.category = category;
        this.weightInGrams = weightInGrams;
        this.quantity = quantity;
        this.priceInEuro = priceInEuro;
        this.productID = productID;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public Integer getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(Integer weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public Double getPriceInEuro() {
        return priceInEuro;
    }

    public void setPriceInEuro(Double priceInEuro) {
        this.priceInEuro = priceInEuro;
    }

    @Override
    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
