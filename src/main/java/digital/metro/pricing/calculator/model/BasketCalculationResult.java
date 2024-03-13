package digital.metro.pricing.calculator.model;

import java.util.Map;

public class BasketCalculationResult {

    private String customerId;
    private Map<String, Price> pricedBasketEntries;
    private Price totalAmount;

    private BasketCalculationResult() {
    }

    public BasketCalculationResult(String customerId, Map<String, Price> pricedBasketEntries, Price totalAmount) {
        this.customerId = customerId;
        this.pricedBasketEntries = pricedBasketEntries;
        this.totalAmount = totalAmount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Map<String, Price> getPricedBasketEntries() {
        return pricedBasketEntries;
    }

    public Price getTotalAmount() {
        return totalAmount;
    }
}
