package digital.metro.pricing.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class BasketEntry {

    private String articleId;
    private BigDecimal quantity;

}
