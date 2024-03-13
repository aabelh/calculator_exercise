package digital.metro.pricing.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class BasketCalculationResult {

    private String customerId;
    private Map<String, Price> pricedBasketEntries;
    private Price totalAmount;

}
