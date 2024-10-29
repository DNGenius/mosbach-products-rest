package mosbach.dhbw.de.products.controller;

import mosbach.dhbw.de.products.data.api.CartManager;
import mosbach.dhbw.de.products.data.api.OrderManager;
import mosbach.dhbw.de.products.data.api.ProductManager;
import mosbach.dhbw.de.products.data.api.TokenManager;
import mosbach.dhbw.de.products.model.OrderRequest;
import mosbach.dhbw.de.products.model.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderManager orderManager;
    private final CartManager cartManager;
    private final TokenManager tokenManager;

    @Autowired
    public OrderController(OrderManager orderManager, CartManager cartManager, TokenManager tokenManager) {
        this.orderManager = orderManager;
        this.cartManager = cartManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping
    public ResponseEntity<?> placeOrderAndClearCart(@RequestHeader("Authorization") String token,
                                                    @RequestBody OrderRequest orderRequest) {

        try {
            if (token != null && token.startsWith("Bearer ")) {
                String tokenValue = token.substring(7);
                if (tokenManager.validateToken(tokenValue)) {
                    String customerID = tokenManager.getUserIDFromToken(tokenValue);
                    long pickupDateMillis = orderRequest.getPickupDate(); // Zeitstempel in Millisekunden
                    // Konvertiere Millisekunden in LocalDate
                    LocalDate pickupDate = Instant.ofEpochMilli(pickupDateMillis)
                            .atZone(ZoneId.systemDefault()) // Hier kannst du die Zeitzone anpassen
                            .toLocalDate();
                    try {
                        orderManager.addOrder(cartManager.getCartByCustomerID(customerID).getCartItems(), cartManager.getTotalPriceByCustomerID(customerID), pickupDate, customerID);
                        cartManager.removeAllProductsFromCart(customerID);
                        return ResponseEntity.ok("Order placed successfully");
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body("The cart is empty");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header must be provided");
            }
        } catch (Exception e) {
            // Unerwarteter Fehler
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
