package mosbach.dhbw.de.products.data.impl;

import mosbach.dhbw.de.products.data.api.Customer;

public class CustomerImpl implements Customer {

    private String userEmail;
    private String userPassword;
    private String lastName;
    private String firstName;
    private String customerID;

    public CustomerImpl (String userEmail, String userPassword, String lastName, String firstName, String customerID) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.lastName = lastName;
        this.firstName = firstName;
        this.customerID = customerID;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }
}
