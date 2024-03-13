package digital.metro.pricing.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

/**
 * Represents the basket content for a customer
 */
@Getter
@AllArgsConstructor
public class Basket {

    private String customerId;
    private Set<BasketEntry> entries;
}
