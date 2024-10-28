package mosbach.dhbw.de.products.controller;

import mosbach.dhbw.de.products.data.api.CustomerManager;
import mosbach.dhbw.de.products.data.api.TokenManager;
import mosbach.dhbw.de.products.data.impl.CustomerImpl;
import mosbach.dhbw.de.products.data.impl.CustomerManagerImpl;
import mosbach.dhbw.de.products.model.Customer;
import mosbach.dhbw.de.products.model.MessageAnswer;
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
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerManager customerManager;
    private final TokenManager tokenManager;

    @Autowired
    public CustomerController(CustomerManager customerManager, TokenManager tokenManager) {
        this.customerManager = customerManager;
        this.tokenManager = tokenManager;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        Logger
                .getLogger("CustomerController")
                .log(Level.INFO, "GET /customer called");

        try {
            List<Customer> myCustomers = new ArrayList<>();

            for (mosbach.dhbw.de.products.data.api.Customer c : customerManager.getAllCustomers()) {
                myCustomers.add(new Customer(
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
                    .getLogger("CustomerController GET Customers")
                    .log(Level.INFO, "Error retrieving customers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {
        Logger.getLogger("CustomerController").log(Level.INFO, "POST /customer called");

        try {
            mosbach.dhbw.de.products.data.api.Customer c = new CustomerImpl(
                    customer.getUserEmail(),
                    customer.getUserPassword(),
                    customer.getLastName(),
                    customer.getFirstName(),
                    customerManager.getNextCustomerId());

            if (customerManager.addCustomer(c)) {
                return ResponseEntity.ok("Successfully registered");
            } else {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("This email is already registered in our system. If you're having trouble accessing your account, please contact our support team for assistance.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Oops! It looks like you missed a field. Please make sure all fields are completed before submitting.");
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops! Something went wrong on our end while processing your registration. Please refresh the page and try again.");
        }
    }
}