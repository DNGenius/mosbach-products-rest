package mosbach.dhbw.de.products.controller;

import mosbach.dhbw.de.products.data.api.ProductManager;
import mosbach.dhbw.de.products.data.api.TokenManager;
import mosbach.dhbw.de.products.data.impl.ProductManagerImpl;
import mosbach.dhbw.de.products.model.Product;
import mosbach.dhbw.de.products.model.SortedProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public SortedProducts getAllProducts(
            @RequestParam(value = "sortOrder", defaultValue = "alphabet") String sortOrder,
            @RequestParam(value = "token", defaultValue = "no-token") String token
    ) {

        Logger
                .getLogger("ProductController")
                .log(Level.INFO, "Bin drin mit sortOrder: " + sortOrder);

        SortedProducts answerSortedProducts = new SortedProducts();
        List<Product> myProducts = new ArrayList<>();

        for (mosbach.dhbw.de.products.data.api.Product p : productManager.getAllProducts()) {
            myProducts.add(new Product(
                    p.getDisplayName(),
                    p.getCategory(),
                    p.getWeightInGrams(),
                    p.getQuantity(),
                    p.getPriceInEuro()
            ));
        }

        if (sortOrder.equals("price")) {
            myProducts.sort(Comparator.comparing(Product::getPriceInEuro));
            answerSortedProducts.setSortOrder("price");
        } else if (sortOrder.equals("alphabet")) {
            myProducts.sort(Comparator.comparing(Product::getDisplayName));
            answerSortedProducts.setSortOrder("alphabet");
        }

        // TODO: Sort the tasks with other sort orders

        Logger.getLogger("ProductController").log(Level.INFO, "Products from file");

        answerSortedProducts.setProducts(myProducts);
        return answerSortedProducts;
    }

    @GetMapping("/{id}")
    public Product getProductByID(@PathVariable("id") String productID) {
        Logger.getLogger("ProductController").log(Level.INFO, "Bin drin mit productID: " + productID);

        mosbach.dhbw.de.products.data.api.Product tempProduct = productManager.getProductByID(productID);
        if (tempProduct == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + productID + " not found.");
        }

        return new Product(
                tempProduct.getDisplayName(),
                tempProduct.getCategory(),
                tempProduct.getWeightInGrams(),
                tempProduct.getQuantity(),
                tempProduct.getPriceInEuro()
        );
    }
}