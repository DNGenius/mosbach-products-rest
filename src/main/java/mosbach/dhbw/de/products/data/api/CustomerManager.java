package mosbach.dhbw.de.products.data.api;

import java.util.List;

public interface CustomerManager {

    boolean addCustomer(Customer customer);
    List<Customer> getAllCustomers();
    void setAllCustomers(List<Customer> customers);
    boolean isEmailTaken(String email);
    String getNextCustomerId();
    boolean authenticateCustomer(String userEmail, String userPassword);
    String getCustomerIDByEmail(String userEmail);

    // TODO deleteCustomer, editCustomer, getCustomerByID?
}
