package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.CartItem;
import mosbach.dhbw.de.products.data.api.Order;
import mosbach.dhbw.de.products.data.api.OrderManager;
import mosbach.dhbw.de.products.data.api.ProductManager;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderManagerImpl implements OrderManager {

    private static OrderManagerImpl orderManagerImpl = null;
    private final ProductManager productManager;
    String fileName =  "orders.properties";

    private OrderManagerImpl() {
        this.productManager = ProductManagerImpl.getProductManagerImpl();
    }

    public static OrderManagerImpl getOrderManagerImpl() {
        if (orderManagerImpl == null) {
            orderManagerImpl = new OrderManagerImpl();
        }
        return orderManagerImpl;
    }

    @Override
    public void addOrder(List<CartItem> orderedItems, Double totalPrice, LocalDate pickupDate, String customerID) {
        // Überprüfe, ob die Liste der bestellten Artikel leer ist
        if (orderedItems == null || orderedItems.isEmpty()) {
            throw new IllegalArgumentException("Die Liste der bestellten Artikel darf nicht leer sein."); // Ausnahme werfen
        }

        List<Order> orders = getAllOrders(); // Alle aktuellen Bestellungen abrufen
        // Neue Bestellung erstellen
        Order newOrder = new OrderImpl(orderedItems, totalPrice, pickupDate, customerID); // Annahme: OrderImpl hat den entsprechenden Konstruktor
        orders.add(newOrder);
        // Speichern der Bestellungen
        setAllOrders(orders);
    }


    @Override
    public List<Order> getAllOrders() {
        Properties properties = new Properties();
        List<Order> tempOrders = new ArrayList<>();
        int orderID = 1; // Zähler für Bestellungen

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream resourceStream = loader.getResourceAsStream(fileName)) {
                properties.load(resourceStream);
            }

            Logger
                    .getLogger("OrderManager")
                    .log(Level.INFO, "Loaded the file");

            // Durchlaufe die Bestellungen und lade sie in die Liste
            while (properties.containsKey("Order." + orderID + ".customerID")) {
                List<CartItem> orderedItems = new ArrayList<>();
                int productCounter = 1; // Zähler für Produkte in der Bestellung

                // Hole alle Produkte in der Bestellung
                while (properties.containsKey("Order." + orderID + ".product." + productCounter + ".productID")) {
                    String productID = properties.getProperty("Order." + orderID + ".product." + productCounter + ".productID");
                    int quantity = Integer.parseInt(properties.getProperty("Order." + orderID + ".product." + productCounter + ".quantity"));

                    // Erstelle CartItem und füge es der Liste hinzu
                    orderedItems.add(new CartItemImpl(productManager.getProductByID(productID), quantity));
                    productCounter++;
                }

                // Lese zusätzliche Informationen der Bestellung
                double totalPrice = Double.parseDouble(properties.getProperty("Order." + orderID + ".totalPrice"));
                LocalDate pickupDate = LocalDate.parse(properties.getProperty("Order." + orderID + ".pickupDate"));
                String customerID = properties.getProperty("Order." + orderID + ".customerID");

                // Erstelle Order-Objekt und füge es zur Liste hinzu
                tempOrders.add(new OrderImpl(orderedItems, totalPrice, pickupDate, customerID));

                orderID++; // Erhöhe den Bestellindex für die nächste Bestellung
            }
        } catch (IOException e) {
            Logger
                    .getLogger("Reading orders in getAllOrders")
                    .log(Level.SEVERE, "Error loading properties file", e);
        }

        return tempOrders;
    }

    @Override
    public void setAllOrders(List<Order> orders) {
        Properties properties = new Properties();

        int orderID = 1; // Zähler für Bestellungen

        for (Order order : orders) {
            List<CartItem> cartItems = order.getOrderedItems(); // Hole die Artikel in der Bestellung
            int productCounter = 1; // Zähler für Produkte in der Bestellung

            // Speichere die CartItems
            for (CartItem item : cartItems) {
                properties.setProperty("Order." + orderID + ".product." + productCounter + ".productID", item.getCartProduct().getProductID());
                properties.setProperty("Order." + orderID + ".product." + productCounter + ".quantity", String.valueOf(item.getQuantity()));
                productCounter++;
            }

            // Speichere die zusätzlichen Informationen der Bestellung
            properties.setProperty("Order." + orderID + ".totalPrice", String.valueOf(order.getTotalPrice()));
            properties.setProperty("Order." + orderID + ".pickupDate", order.getPickupDate().toString()); // LocalDate in String konvertieren
            properties.setProperty("Order." + orderID + ".customerID", order.getCustomerID());

            orderID++; // Erhöhe den Bestellindex für die nächste Bestellung
        }

        try (FileOutputStream output = new FileOutputStream(fileName)) {
            properties.store(output, null);
            Logger
                    .getLogger("Writing Orders in setAllOrders")
                    .log(Level.INFO, "Orders saved successfully");
        } catch (IOException e) {
            Logger
                    .getLogger("Writing Orders in setAllOrders")
                    .log(Level.SEVERE, "File cannot be written", e);
        }
    }

}
