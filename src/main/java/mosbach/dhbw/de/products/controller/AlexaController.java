package mosbach.dhbw.de.products.controller;

import mosbach.dhbw.de.products.data.api.Product;
import mosbach.dhbw.de.products.data.api.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/alexa")
public class AlexaController {

    private final ProductManager productManager;

    @Autowired
    public AlexaController(ProductManager productManager) {
        this.productManager = productManager;
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProductsShuffled() {
        try {
            List<Product> answerSortedProducts = productManager.getAllProducts();
            Collections.shuffle(answerSortedProducts);
            Logger
                    .getLogger("AlexaController")
                    .log(Level.INFO, "Products mixed");
            return ResponseEntity.ok(answerSortedProducts);
        } catch (Exception e) {
            // Unerwarteter Fehler
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
