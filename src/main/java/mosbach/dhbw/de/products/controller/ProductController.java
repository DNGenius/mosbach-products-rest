package mosbach.dhbw.de.products.controller;

import mosbach.dhbw.de.products.data.api.Product;
import mosbach.dhbw.de.products.data.api.ProductManager;
import mosbach.dhbw.de.products.data.api.TokenManager;
import mosbach.dhbw.de.products.data.impl.ProductImpl;
import mosbach.dhbw.de.products.data.impl.ProductManagerImpl;
import mosbach.dhbw.de.products.model.ProductRequest;
import mosbach.dhbw.de.products.model.SortOrderRequest;
import mosbach.dhbw.de.products.model.SortedProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductManager productManager;
    private final TokenManager tokenManager;

    @Autowired
    public ProductController(ProductManager productManager, TokenManager tokenManager) {
        this.productManager = productManager;
        this.tokenManager = tokenManager;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestHeader("Authorization") String token,
                                            @RequestBody SortOrderRequest sortOrderRequest) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String tokenValue = token.substring(7);
                if (tokenManager.validateToken(tokenValue)) {
                    String sortOrder = sortOrderRequest.getSortOrder();
                    Logger
                            .getLogger("ProductController")
                            .log(Level.INFO, "Bin drin mit sortOrder: " + sortOrder);

                    SortedProducts answerSortedProducts = new SortedProducts();
                    List<Product> myProducts = productManager.getAllProducts();

                    if (sortOrder.equals("price")) {
                        myProducts.sort(Comparator.comparing(Product::getPriceInEuro));
                        answerSortedProducts.setSortOrder("price");
                    } else if (sortOrder.equals("alphabet")) {
                        myProducts.sort(Comparator.comparing(Product::getDisplayName));
                        answerSortedProducts.setSortOrder("alphabet");
                    }

                    // TODO: Sort the tasks with other sort orders

                    Logger
                            .getLogger("ProductController")
                            .log(Level.INFO, "Products sorted");
                    answerSortedProducts.setProducts(myProducts);
                    return ResponseEntity.ok(answerSortedProducts);
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

    @GetMapping("/{id}")
    public Product getProductByID(@PathVariable("id") String productID) {
        Logger
                .getLogger("ProductController")
                .log(Level.INFO, "Bin drin mit productID: " + productID);

        Product tempProduct = productManager.getProductByID(productID);
        if (tempProduct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + productID + " not found.");
        }

        return new ProductImpl(
                tempProduct.getDisplayName(),
                tempProduct.getCategory(),
                tempProduct.getWeightInGrams(),
                tempProduct.getTotalQuantity(),
                tempProduct.getPriceInEuro(),
                tempProduct.getProductID()
        );
    }
}