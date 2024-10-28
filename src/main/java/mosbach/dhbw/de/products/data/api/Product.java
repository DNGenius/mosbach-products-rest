package mosbach.dhbw.de.products.data.api;

public interface Product {

    String getDisplayName();
    String getCategory();
    Integer getWeightInGrams();
    Integer getTotalQuantity();
    Double getPriceInEuro();
    String getProductID();
}
