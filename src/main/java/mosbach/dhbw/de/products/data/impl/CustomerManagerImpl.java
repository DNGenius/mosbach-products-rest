package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.Customer;
import mosbach.dhbw.de.products.data.api.CustomerManager;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CustomerManagerImpl implements CustomerManager {

    private static CustomerManagerImpl customerManagerImpl = null;
    String fileName = "customers.properties";

    private CustomerManagerImpl() {
    }

    public static CustomerManagerImpl getCustomerManagerImpl() {
        if (customerManagerImpl == null) {
            customerManagerImpl = new CustomerManagerImpl();
        }
        return customerManagerImpl;
    }

    @Override
    public boolean addCustomer(Customer customer) {
        if (customer.getUserEmail() == null || customer.getUserPassword() == null
                || customer.getLastName() == null || customer.getFirstName() == null) {
            throw new IllegalArgumentException("All attributes must be provided");
        }

        try {
            if (isEmailTaken(customer.getUserEmail())) {
                return false;
            }
        } catch (RuntimeException e) {
            // Fehler beim Überprüfen der E-Mail von isEmailTaken - wird an createCustomer weitergegeben
            throw new RuntimeException("Error checking email availability: " + e.getMessage(), e);
        }

        List<Customer> customers = getAllCustomers();
        customers.add(customer);
        setAllCustomers(customers);
        return true;
    }

    @Override
    public List<Customer> getAllCustomers() {

        Properties properties = new Properties();
        List<Customer> tempCustomers = new ArrayList<>();
        int i = 1;

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try(InputStream resourceStream = loader.getResourceAsStream(fileName)) {
                properties.load(resourceStream);
            }

            Logger
                    .getLogger("CustomerManager")
                    .log(Level.INFO, "Loaded the file");

            while (properties.containsKey("Customer." + i + ".userEmail")) {
                String userEmail = properties.getProperty("Customer." + i + ".userEmail");
                String userPassword = properties.getProperty("Customer." + i + ".userPassword");
                String lastName = properties.getProperty("Customer." + i + ".lastName");
                String firstName = properties.getProperty("Customer." + i + ".firstName");
                String customerID = properties.getProperty("Customer." + i + ".customerID");

                Logger
                        .getLogger("CustomerManager")
                        .log(Level.INFO, "Found a customer");

                tempCustomers.add(new CustomerImpl(userEmail, userPassword, lastName, firstName, customerID));
                i++;
            }
        } catch (IOException e) {
            Logger
                    .getLogger("Reading customers in getAllCustomers")
                    .log(Level.INFO, "Error loading properties file");
        }

        return tempCustomers;
    }

    @Override
    public void setAllCustomers(List<Customer> customers) {

        Properties properties = new Properties();

        int i = 1;
        for(Customer c : customers){
            properties.setProperty("Customer." + i + ".userEmail", c.getUserEmail());
            properties.setProperty("Customer." + i + ".userPassword", c.getUserPassword());
            properties.setProperty("Customer." + i + ".lastName", c.getLastName());
            properties.setProperty("Customer." + i + ".firstName", c.getFirstName());
            properties.setProperty("Customer." + i + ".customerID", c.getCustomerID());
            i++;
        }

        try (FileOutputStream output = new FileOutputStream(fileName)) {
            properties.store(output, null);
            Logger
                    .getLogger("Writing Customers in setAllCustomers")
                    .log(Level.INFO, "Customers saved successfully");
        } catch (IOException e) {
            Logger
                    .getLogger("Writing Customers in setAllCustomers")
                    .log(Level.INFO, "File cannot be written");
        }
    }

    @Override
    public boolean isEmailTaken(String email) {
        Properties properties = new Properties();

        try {

            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try(InputStream resourceStream = loader.getResourceAsStream(fileName)) {
                properties.load(resourceStream);
            }

            for (String key : properties.stringPropertyNames()) {
                if (key.endsWith(".userEmail") && properties.getProperty(key).equals(email)) {
                    return true;
                }
            }

        } catch (IOException e) {
            Logger
                    .getLogger("Reading customers in isEmailTaken")
                    .log(Level.INFO, "Error loading properties file");
            // Fehler beim Laden der Properties - wird an addCustomer weitergegeben
            throw new RuntimeException("Failed to load properties file", e);
        }
        return false;
    }

    @Override
    public String getNextCustomerID() {

        Properties properties = new Properties();
        int maxId = 0;

        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (InputStream resourceStream = loader.getResourceAsStream(fileName)) {
                properties.load(resourceStream);
            }

            for (String key : properties.stringPropertyNames()) {
                if (key.startsWith("Customer.") && key.endsWith(".userEmail")) {
                    String[] parts = key.split("\\.");
                    if (parts.length > 1) {
                        try {
                            int id = Integer.parseInt(parts[1]);
                            maxId = Math.max(maxId, id);
                        } catch (NumberFormatException e) {
                            // Fehler beim parsen der customerID
                        }
                    }
                }
            }
        } catch (IOException e) {
            Logger
                    .getLogger("Reading customers in getNextCustomerId")
                    .log(Level.INFO, "Error loading properties file", e);
            throw new RuntimeException("Failed to load properties file", e);
        }

        return String.valueOf(maxId + 1);
    }

    @Override
    public boolean authenticateCustomer(String userEmail, String userPassword) {
        try {
            List<Customer> customers = getAllCustomers();

            for (Customer customer : customers) {
                if (customer.getUserEmail().equals(userEmail) && customer.getUserPassword().equals(userPassword)) {
                    Logger
                            .getLogger("CustomerManager")
                            .log(Level.INFO, "Customer authenticated: " + userEmail);
                    return true;
                }
            }
        } catch (RuntimeException e) {
            Logger
                    .getLogger("Getting customers in getNextCustomerId")
                    .log(Level.INFO, "Error loading properties file");
        }
        Logger
                .getLogger("CustomerManager")
                .log(Level.INFO, "Authentication failed for: " + userEmail);
        return false;
    }

    @Override
    public String getCustomerIDByEmail(String userEmail) {
        String customerID = "";
        try {
            List<Customer> customers = getAllCustomers();

            for (Customer customer : customers) {
                if (customer.getUserEmail().equals(userEmail)) {
                    customerID = customer.getCustomerID();
                    Logger
                            .getLogger("CustomerManager")
                            .log(Level.INFO, "CustomerID: " + customerID + " found for email: " + userEmail);
                }
            }

        } catch (RuntimeException e) {
            Logger
                    .getLogger("Getting customers in getCustomerIDByEmail")
                    .log(Level.INFO, "Error loading properties file");
        }
        return customerID;
    }

}
