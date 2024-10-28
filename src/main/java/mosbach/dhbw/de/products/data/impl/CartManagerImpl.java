package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.*;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CartManagerImpl implements CartManager {

    private static CartManagerImpl cartManagerImpl = null;
    private final ProductManager productManager;
    String fileName = "carts.properties";

    private CartManagerImpl() {
        this.productManager = ProductManagerImpl.getProductManagerImpl();
    }

    public static CartManagerImpl getCartManagerImpl() {
        if (cartManagerImpl == null) {
            cartManagerImpl = new CartManagerImpl();
        }
        return cartManagerImpl;
    }

    public void addProductToCart(String customerID, Product selectedProduct, Integer quantity) {
        List<Cart> carts = getAllCarts();
        Cart customerCart = getCartByCustomerID(customerID);

        // Wenn kein Warenkorb existiert, einen neuen erstellen
        if (customerCart == null) {
            customerCart = new CartImpl(new ArrayList<>(), customerID, customerID);
            carts.add(customerCart);
        }

        // Produkt zum Warenkorb hinzufügen
        List<CartItem> items = customerCart.getCartItems();
        boolean productExists = false;

        // Wenn Produkt vorhanden, Menge erhöhen
        for (CartItem item : items) {
            if (item.getCartProduct().equals(selectedProduct)) {
                item.setQuantity(item.getQuantity() + quantity);
                productExists = true;
                break;
            }
        }

        // Wenn Produkt nicht vorhanden, ein neues hinzufügen
        if (!productExists) {
            items.add(new CartItemImpl(selectedProduct, quantity));
        }

        // Alle Warenkörbe speichern
        setAllCarts(carts);
    }

    @Override
    public boolean removeProductFromCart(String customerID, String productID) {
        List<Cart> allCarts = getAllCarts();
        boolean productRemoved = false;

        for (Cart cart : allCarts) {
            if (cart.getCustomerID().equals(customerID)) {
                List<CartItem> items = cart.getCartItems();
                Iterator<CartItem> iterator = items.iterator();
                while (iterator.hasNext()) {
                    CartItem item = iterator.next();
                    if (item.getCartProduct().getProductID().equals(productID)) {
                        iterator.remove();
                        productRemoved = true;
                        break;
                    }
                }
                break;
            }
        }

        if (productRemoved) {
            // Speichern Sie die Änderungen am Warenkorb
            setAllCarts(allCarts);
            return true;
        }

        return false;
    }


    @Override
    public List<Cart> getAllCarts() {
        Properties properties = new Properties();
        List<Cart> carts = new ArrayList<>();
        int i = 1;

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream resourceStream = loader.getResourceAsStream(fileName)) {
                properties.load(resourceStream);
            }

            Logger
                    .getLogger("CartManager")
                    .log(Level.INFO, "Loaded the file");

            while (properties.containsKey("Cart." + i + ".cartID")) {
                String cartID = properties.getProperty("Cart." + i + ".cartID");
                String customerID = properties.getProperty("Cart." + i + ".customerID");
                List<CartItem> cartItems= new ArrayList<>();
                int j = 1;

                while (properties.containsKey("Cart." + i + ".product." + j + ".productID")) {
                    String productID = properties.getProperty("Cart." + i + ".product." + j + ".productID");
                    Product cartProduct = productManager.getProductByID(productID);
                    Integer quantity = Integer.parseInt(properties.getProperty("Cart." + i + ".product." + j + ".quantity"));
                    cartItems.add(new CartItemImpl(cartProduct, quantity));
                    j++;
                }

                carts.add(new CartImpl(cartItems, cartID, customerID));
                i++;
            }
        } catch (IOException e) {
            Logger.getLogger("Reading carts in getAllCarts").log(Level.INFO, "Error loading properties file");
        }

        return carts;
    }

    @Override
    public void setAllCarts(List<Cart> carts) {
        Properties properties = new Properties();
        int i = 1;

        for (Cart cart : carts) {
            int j = 1;

            // Zuerst die Produkte speichern
            for (CartItem item : cart.getCartItems()) {
                properties.setProperty("Cart." + i + ".product." + j + ".productID", String.valueOf(item.getCartProduct().getProductID()));
                properties.setProperty("Cart." + i + ".product." + j + ".quantity", String.valueOf(item.getQuantity()));
                j++;
            }

            // Danach cartID und customerID speichern
            properties.setProperty("Cart." + i + ".cartID", String.valueOf(cart.getCartID()));
            properties.setProperty("Cart." + i + ".customerID", String.valueOf(cart.getCustomerID()));

            i++;
        }

        try (FileOutputStream output = new FileOutputStream(fileName)) {
            properties.store(output, null);
            Logger
                    .getLogger("Writing Carts in setAllCarts")
                    .log(Level.INFO, "Carts saved successfully");
        } catch (IOException e) {
            Logger
                    .getLogger("Writing Carts in setAllCarts")
                    .log(Level.INFO, "File cannot be written");
        }
    }

    public Cart getCartByCustomerID(String customerID) {
        Cart answerCart = null;
        try {
            List<Cart> carts = getAllCarts();

            for (Cart cart : carts) {
                if (cart.getCustomerID().equals(customerID)) {
                    Logger
                            .getLogger("CartManager")
                            .log(Level.INFO, "Cart found for customerID: " + customerID);
                    answerCart = cart;
                }
            }

        } catch (RuntimeException e) {
            Logger
                    .getLogger("Getting Carts in getCartByUserID")
                    .log(Level.INFO, "Error loading properties file");
        }
        return answerCart;
    }

    public Double getTotalPriceByCustomerID(String customerID) {

        Double answerTotalPrice = 0.0;
        List<CartItem> cartItems = getCartByCustomerID(customerID).getCartItems();
        for (CartItem cartItem : cartItems) {
            answerTotalPrice += cartItem.getCartProduct().getPriceInEuro() * cartItem.getQuantity();
        }
        return answerTotalPrice;
    }
}
