/*package mosbach.dhbw.de.products.controller;
import mosbach.dhbw.de.products.data.api.CustomerManager;
import mosbach.dhbw.de.products.data.api.ProductManager;
import mosbach.dhbw.de.products.data.impl.CustomerImpl;
import mosbach.dhbw.de.products.data.impl.CustomerManagerImpl;
import mosbach.dhbw.de.products.data.impl.ProductManagerImpl;
import mosbach.dhbw.de.products.model.Customer;
import mosbach.dhbw.de.products.model.Product;
import mosbach.dhbw.de.products.model.SortedProducts;
import mosbach.dhbw.de.products.model.MessageAnswer;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/api")
public class MappingController {

    // TODO: Write other managers
    ProductManager productManager = ProductManagerImpl.getProductManagerImpl();
    CustomerManager customerManager = CustomerManagerImpl.getCustomerManagerImpl();

    // TODO: When ready for using a database manager, switch to database use
    // CustomerManager customerManager = PostgresDBCustomerManagerImpl.getCustomerManagerImpl();

    public MappingController(
    ) {
    }

    @GetMapping("/auth")
    public String getAuthServerAlive() {
        return
                "The WildeWurstWaren Shop is alive.";
    }

    @GetMapping("/customer")
    public ResponseEntity<List<Customer>> getAllCustomers() {

        Logger
                .getLogger("MappingController")
                .log(Level.INFO, "GET /customer called");

        try {
            List<mosbach.dhbw.de.products.model.Customer> myCustomers = new ArrayList<>();

            for (mosbach.dhbw.de.products.data.api.Customer c : customerManager.getAllCustomers()) {
                myCustomers.add(new mosbach.dhbw.de.products.model.Customer(
                        c.getUserEmail(),
                        c.getUserPassword(),
                        c.getLastName(),
                        c.getFirstName()
                ));
            }

            if (myCustomers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(myCustomers);
        } catch (Exception e) {
            Logger
                    .getLogger("MappingController GET Customers")
                    .log(Level.INFO, "Error retrieving customers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(
            path = "/customer",
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public MessageAnswer createCustomer(@RequestBody Customer customer) {

        Logger
                .getLogger("MappingController")
                .log(Level.INFO, "POST /customer called");

        String message = "";

        try {

            mosbach.dhbw.de.products.data.api.Customer c = new CustomerImpl(
                    customer.getUserEmail(),
                    customer.getUserPassword(),
                    customer.getLastName(),
                    customer.getFirstName(),
                    customerManager.getNextCustomerId());

            if (customerManager.addCustomer(c)) {
                message = "Successfully registered";
            }
            else {
                message = "This email is already registered in our system. If you're having trouble accessing your account, please contact our support team for assistance.";
            }

        } catch (IllegalArgumentException e) {
            message = "Oops! It looks like you missed a field. Please make sure all fields are completed before submitting.";
        } catch (RuntimeException e) {
            message = "Oops! Something went wrong on our end while processing your registration. Please refresh the page and try again.";
        }

        return
                new MessageAnswer(message);
    }

    @GetMapping("/products")
    public SortedProducts getAllProducts(
            @RequestParam(value = "sortOrder", defaultValue = "alphabet") String sortOrder,
            @RequestParam(value = "token", defaultValue = "no-token") String token
    ) {

        // TODO: Check the token with your TokenManager

        Logger
                .getLogger("MappingController")
                .log(Level.INFO, "Bin drin mit sortOrder: " + sortOrder);

        SortedProducts answerSortedProducts = new SortedProducts();

        List<Product> myProducts = new ArrayList<>();

        for (mosbach.dhbw.de.products.data.api.Product p : productManager.getAllProducts())
            myProducts.add(new Product(
                    p.getDisplayName(),
                    p.getCategory(),
                    p.getWeightInGrams(),
                    p.getQuantity(),
                    p.getPriceInEuro()
            ));

        if (sortOrder.equals("price")) {
            myProducts.sort(Comparator.comparing(Product::getPriceInEuro));
            answerSortedProducts.setSortOrder("price");
        }
        else if (sortOrder.equals("alphabet")) {
            myProducts.sort(Comparator.comparing(Product::getDisplayName));
            answerSortedProducts.setSortOrder("alphabet");
        }

        // TODO: Sort the tasks with other sort orders

        Logger
                .getLogger("MappingController")
                .log(Level.INFO, "Products from file");

        answerSortedProducts.setProducts(myProducts);
        return
                answerSortedProducts;
    }

    @GetMapping("/products/{id}")
    public Product getProductByID(@PathVariable("id") String productID) throws Exception {

        Logger
                .getLogger("MappingController")
                .log(Level.INFO, "Bin drin mit productID: " + productID);


        if (productManager.getProductByID(productID) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with ID " + productID + " not found.");
        }

        mosbach.dhbw.de.products.data.api.Product tempProduct = productManager.getProductByID(productID);
            Product answerProduct = (new Product(
                    tempProduct.getDisplayName(),
                    tempProduct.getCategory(),
                    tempProduct.getWeightInGrams(),
                    tempProduct.getQuantity(),
                    tempProduct.getPriceInEuro()
            ));

        return
                answerProduct;
    }
}*/
