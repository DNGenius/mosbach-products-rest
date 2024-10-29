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

    @Override
    public void createNewCart(String customerID) {
        List<Cart> carts = getAllCarts();
        // Neuen Warenkorb mit cartID = customerID erstellen
        Cart customerCart = new CartImpl(new ArrayList<>(), customerID, customerID);
        carts.add(customerCart);
        // Alle Warenkörbe speichern
        setAllCarts(carts);
    }

    @Override
    public void addProductToCart(String customerID, Product selectedProduct, Integer quantity) {
        List<Cart> carts = getAllCarts(); // Hole alle aktuellen Warenkörbe
        Cart customerCart = getCartByCustomerID(customerID); // Hole Warenkorb des Kunden
        boolean productExists = false;

        // Produkte im Warenkorb
        List<CartItem> items = customerCart.getCartItems();

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

        // Aktualisiere den Kunden-Warenkorb in der Liste aller Warenkörbe
        for (Cart cart: carts) {
            if (cart.getCustomerID().equals(customerID)) {
                cart.setCartItems(items); // Aktualisiert den Warenkorb in der Liste
                break;
            }
        }

        // Speichere alle Warenkörbe
        setAllCarts(carts);
    }

    @Override
    public boolean removeProductFromCart(String customerID, String productID) {
        List<Cart> carts = getAllCarts(); // Hole alle aktuellen Warenkörbe
        Cart customerCart = getCartByCustomerID(customerID); // Hole Warenkorb des Kunden
        boolean productRemoved = false;

        // Produkte im Warenkorb
        List<CartItem> items = customerCart.getCartItems();

        // Wenn Produkt vorhanden, Menge erhöhen
        for (CartItem item : items) {
            if (item.getCartProduct().getProductID().equals(productID)) {
                items.remove(item);
                productRemoved = true;
                break;
            }
        }

        if (productRemoved) {
            // Aktualisiere den Kunden-Warenkorb in der Liste aller Warenkörbe
            for (Cart cart: carts) {
                if (cart.getCustomerID().equals(customerID)) {
                    cart.setCartItems(items); // Aktualisiert den Warenkorb in der Liste
                    break;
                }
            }
            // Speichere alle Warenkörbe
            setAllCarts(carts);
            return true;
        }

        return false;
    }

    @Override
    public void removeAllProductsFromCart(String customerID) {
        List<Cart> carts = getAllCarts(); // Hole alle aktuellen Warenkörbe
        Cart customerCart = getCartByCustomerID(customerID); // Hole Warenkorb des Kunden

        // Überprüfe, ob der Warenkorb des Kunden existiert
        if (customerCart.getCartItems().isEmpty()) {
            return; // Kunden-Warenkorb existiert nicht
        }

        // Leere die Liste der CartItems
        List<CartItem> emptyItems = new ArrayList<>();
        customerCart.setCartItems(emptyItems); // Setze die CartItems auf eine leere Liste

        // Aktualisiere den Kunden-Warenkorb in der Liste aller Warenkörbe
        for (Cart cart : carts) {
            if (cart.getCustomerID().equals(customerID)) {
                cart.setCartItems(emptyItems); // Aktualisiert den Warenkorb in der Liste
                break;
            }
        }

        // Speichere alle Warenkörbe
        setAllCarts(carts);
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
                List<CartItem> cartItems = new ArrayList<>();
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
