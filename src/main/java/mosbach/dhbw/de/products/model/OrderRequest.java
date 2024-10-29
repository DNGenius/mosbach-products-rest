
package mosbach.dhbw.de.products.model;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequest {

    @JsonProperty("customerID")
    private String customerID;
    @JsonProperty("pickupDate")
    private long pickupDate;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public OrderRequest() {
    }

    public OrderRequest(String customerID, long pickupDate) {
        super();
        this.customerID = customerID;
        this.pickupDate = pickupDate;
    }

    @JsonProperty("customerID")
    public String getCustomerID() {
        return customerID;
    }

    @JsonProperty("customerID")
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    @JsonProperty("pickupDate")
    public long getPickupDate() {
        return pickupDate;
    }

    @JsonProperty("pickupDate")
    public void setPickupDate(long pickupDate) {
        this.pickupDate = pickupDate;
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
