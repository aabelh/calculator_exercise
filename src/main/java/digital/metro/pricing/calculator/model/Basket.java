package digital.metro.pricing.calculator.model;

import java.util.Set;

/**
 * Represents the basket content for a customer
 */
public class Basket {

    private String customerId;
    private Set<BasketEntry> entries;

    public Basket(String customerId, Set<BasketEntry> entries) {
        this.customerId = customerId;
        this.entries = entries;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Set<BasketEntry> getEntries() {
        return entries;
    }
}
