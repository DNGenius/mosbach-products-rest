package mosbach.dhbw.de.products.data.api;

import mosbach.dhbw.de.products.data.api.Product;

import java.util.List;

public interface ProductManager {

    List<Product> getAllProducts();
    Product getProductByID(String productID);

}
