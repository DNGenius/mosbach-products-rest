
package mosbach.dhbw.de.products.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerCart {

    @JsonProperty("cartItems")
    private List<CartItem> cartItems;
    @JsonProperty("totalPrice")
    private Double totalPrice;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public CustomerCart() {
    }

    public CustomerCart(List<CartItem> cartItems, Double totalPrice) {
        super();
        this.cartItems = cartItems;
        this.totalPrice = totalPrice;
    }

    @JsonProperty("cartItems")
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    @JsonProperty("cartItems")
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @JsonProperty("totalPrice")
    public Double getTotalPrice() {
        return totalPrice;
    }

    @JsonProperty("totalPrice")
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
