package mosbach.dhbw.de.products.controller;

import mosbach.dhbw.de.products.data.api.*;
import mosbach.dhbw.de.products.data.impl.CustomerImpl;
import mosbach.dhbw.de.products.data.impl.CustomerManagerImpl;
import mosbach.dhbw.de.products.model.Customer;
import mosbach.dhbw.de.products.model.CustomerCart;
import mosbach.dhbw.de.products.model.MessageAnswer;
import mosbach.dhbw.de.products.model.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartManager cartManager;
    private final ProductManager productManager;
    private final TokenManager tokenManager;

    @Autowired
    public CartController(CartManager cartManager, ProductManager productManager, TokenManager tokenManager) {
        this.cartManager = cartManager;
        this.productManager = productManager;
        this.tokenManager = tokenManager;
    }

    @GetMapping("/view")
    public ResponseEntity<?> getCartItems(@RequestHeader("Authorization") String token) {

        try {
            if (token != null && token.startsWith("Bearer ")) {
                String tokenValue = token.substring(7);
                Logger
                        .getLogger("CartController GET")
                        .log(Level.INFO, "tokenValue extracted");

                if (tokenManager.validateToken(tokenValue)) {
                    Logger
                            .getLogger("CartController GET")
                            .log(Level.INFO, "token validated");
                    String customerID = tokenManager.getUserIDFromToken(tokenValue);
                    Logger
                            .getLogger("CartController GET")
                            .log(Level.INFO, "Token of CustomerID: " + customerID);
                    // Abrufen des Warenkorbs für die customerID
                    Cart cart = cartManager.getCartByCustomerID(customerID);
                    if (cart == null || cart.getCartItems().isEmpty()) {
                        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("The cart is empty");
                    }
                    // Convertiert CartItems (api) zu CartItems (model) für Rückgabe
                    List<mosbach.dhbw.de.products.model.CartItem> answerCartItems = new ArrayList<>();
                    for (CartItem c : cartManager.getCartByCustomerID(customerID).getCartItems()) {
                        answerCartItems.add(new mosbach.dhbw.de.products.model.CartItem
                                (new mosbach.dhbw.de.products.model.Product(
                                        c.getCartProduct().getDisplayName(),
                                        c.getCartProduct().getCategory(),
                                        c.getCartProduct().getWeightInGrams(),
                                        c.getCartProduct().getTotalQuantity(),
                                        c.getCartProduct().getPriceInEuro(),
                                        c.getCartProduct().getProductID()),
                                        c.getQuantity()));
                    }

                    CustomerCart answerCustomerCart = new CustomerCart (answerCartItems, cartManager.getTotalPriceByCustomerID(customerID));
                    return ResponseEntity.ok(answerCustomerCart);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header must be provided");
            }
        } catch (Exception e) {
            // Unerwarteter Fehler
            Logger
                    .getLogger("CartController")
                    .log(Level.SEVERE, "Error in getCartItems", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addProductToCart(@RequestHeader("Authorization") String token,
                                                   @RequestBody ProductRequest productRequest) {

        try {
            if (token != null && token.startsWith("Bearer ")) {
                String tokenValue = token.substring(7);
                if (tokenManager.validateToken(tokenValue)) {
                    String customerID = tokenManager.getUserIDFromToken(tokenValue);
                    cartManager.addProductToCart(customerID, productManager.getProductByID(productRequest.getProductID()), productRequest.getQuantity());
                    return ResponseEntity.ok("Product added to cart");
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

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestHeader("Authorization") String token,
                                                        @RequestParam String productID) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String tokenValue = token.substring(7);
                if (tokenManager.validateToken(tokenValue)) {
                    String customerID = tokenManager.getUserIDFromToken(tokenValue);
                    boolean removed = cartManager.removeProductFromCart(customerID, productID);
                    if (removed) {
                        return ResponseEntity.ok("Product removed from cart");
                    } else {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found in cart");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Authorization header must be provided");
            }
        } catch (Exception e) {
            Logger.getLogger("CartController")
                    .log(Level.SEVERE, "Error in removeProductFromCart", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

}
