package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.Product;
import mosbach.dhbw.de.products.data.api.ProductManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductManagerImpl implements ProductManager {

    private static ProductManagerImpl productManagerImpl = null;
    String fileName =  "products.properties";

    private ProductManagerImpl() { }

    public static ProductManagerImpl getProductManagerImpl() {
        if (productManagerImpl == null) {
            productManagerImpl = new ProductManagerImpl();
        }
        return productManagerImpl;
    }

    @Override
    public List<Product> getAllProducts() {

        Properties properties = new Properties();
        List<Product> tempProducts = new ArrayList<>();
        int i = 1;

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try(InputStream resourceStream = loader.getResourceAsStream(fileName)) {
                properties.load(resourceStream);
            }

            Logger
                    .getLogger("ProductManager")
                    .log(Level.INFO, "Loaded the file");

            while (properties.containsKey("Product." + i + ".displayName")) {
                String displayName = properties.getProperty("Product." + i + ".displayName");
                String category =  properties.getProperty("Product." + i + ".category");
                Integer weightInGrams  = Integer.parseInt(properties.getProperty("Product." + i + ".weightInGrams"));
                String totalQuantityStr = properties.getProperty("Product." + i + ".totalQuantity");
                Integer totalQuantity = (totalQuantityStr != null && !totalQuantityStr.isEmpty())
                        ? Integer.parseInt(totalQuantityStr)
                        : Integer.MAX_VALUE; // Wert auf den totalQuantity gesetzt wird, falls keiner angegeben ist
                Double priceInEuro = Double.parseDouble(properties.getProperty("Product." + i + ".priceInEuro"));
                String productID = properties.getProperty("Product." + i + ".productID");

                Logger
                        .getLogger("ProductManager")
                        .log(Level.INFO, "Found a product");

                tempProducts.add(new ProductImpl(displayName, category, weightInGrams, totalQuantity, priceInEuro, productID));
                i++;
            }
        } catch (IOException e) {
            Logger
                    .getLogger("Reading Products")
                    .log(Level.INFO, "File not exising");
        }

        return tempProducts;
    }

    @Override
    public Product getProductByID(String productID) {

        Properties properties = new Properties();
        ProductImpl tempProduct = null;

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try(InputStream resourceStream = loader.getResourceAsStream(fileName)) {
                properties.load(resourceStream);
            }

            Logger
                    .getLogger("ProductManager")
                    .log(Level.INFO, "Loaded the file");


            String displayName = properties.getProperty("Product." + productID + ".displayName");
            String category =  properties.getProperty("Product." + productID + ".category");
            Integer weightInGrams  = Integer.parseInt(properties.getProperty("Product." + productID + ".weightInGrams"));
            String totalQuantityStr = properties.getProperty("Product." + productID + ".totalQuantity");
            Integer totalQuantity = (totalQuantityStr != null && !totalQuantityStr.isEmpty())
                    ? Integer.parseInt(totalQuantityStr)
                    : Integer.MAX_VALUE; // Wert auf den totalQuantity gesetzt wird, falls keiner angegeben ist
            Double priceInEuro = Double.parseDouble(properties.getProperty("Product." + productID + ".priceInEuro"));

            Logger
                    .getLogger("ProductManager")
                    .log(Level.INFO, "Found the product");

            tempProduct = new ProductImpl(displayName, category, weightInGrams, totalQuantity, priceInEuro, productID);

        } catch (IOException e) {
            Logger
                    .getLogger("Reading Products")
                    .log(Level.INFO, "File not exising");
        }
        catch (NumberFormatException e) {
            Logger.getLogger("ProductManager")
                    .log(Level.SEVERE, "Error parsing product data for ID: " + productID + " - " + e.getMessage(), e);
        }

        return tempProduct;
    }
}
