
package mosbach.dhbw.de.products.model;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SortedProducts {

    @JsonProperty("sort-order")
    private String sortOrder;
    @JsonProperty("products")
    private List<Product> products;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public SortedProducts() {
    }

    /**
     * 
     * @param sortOrder
     * @param products
     */
    public SortedProducts(String sortOrder, List<Product> products) {
        super();
        this.sortOrder = sortOrder;
        this.products = products;
    }

    @JsonProperty("sort-order")
    public String getSortOrder() {
        return sortOrder;
    }

    @JsonProperty("sort-order")
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    @JsonProperty("products")
    public List<Product> getProducts() {
        return products;
    }

    @JsonProperty("products")
    public void setProducts(List<Product> products) {
        this.products = products;
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
