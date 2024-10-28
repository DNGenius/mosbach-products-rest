
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
public class Product {

    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("category")
    private String category;
    @JsonProperty("weightInGrams")
    private Integer weightInGrams;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("priceInEuro")
    private Double priceInEuro;
    @JsonProperty("productID")
    private String productID;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Product() {
    }

    public Product(String displayName, String category, Integer weightInGrams, Integer quantity, Double priceInEuro, String productID) {
        super();
        this.displayName = displayName;
        this.category = category;
        this.weightInGrams = weightInGrams;
        this.quantity = quantity;
        this.priceInEuro = priceInEuro;
        this.productID = productID;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(String category) {
        this.category = category;
    }

    @JsonProperty("weightInGrams")
    public Integer getWeightInGrams() {
        return weightInGrams;
    }

    @JsonProperty("weightInGrams")
    public void setWeightInGrams(Integer weightInGrams) {
        this.weightInGrams = weightInGrams;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("priceInEuro")
    public Double getPriceInEuro() {
        return priceInEuro;
    }

    @JsonProperty("priceInEuro")
    public void setPriceInEuro(Double priceInEuro) {
        this.priceInEuro = priceInEuro;
    }

    @JsonProperty("productID")
    public String getProductID() { return productID; }

    @JsonProperty("productID")
    public void setProductID(String productID) { this.productID = productID; }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
