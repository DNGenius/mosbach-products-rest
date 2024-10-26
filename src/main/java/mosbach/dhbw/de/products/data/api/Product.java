package mosbach.dhbw.de.products.data.api;

public interface Product {

    String getDisplayName();
    String getCategory();
    Integer getWeightInGrams();
    Integer getQuantity();
    Double getPriceInEuro();
    String getProductID();
}
