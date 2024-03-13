package digital.metro.pricing.calculator.repository;

import digital.metro.pricing.calculator.model.Price;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A dummy implementation for testing purposes. In production, we would get real prices from a database.
 */
@Repository
public class PriceRepository {

    private Map<String, Price> prices = new HashMap<>();
    private Random random = new Random();

    public Price priceByArticleId(String articleId) {
        return prices.computeIfAbsent(articleId,
                key -> new Price(BigDecimal.valueOf(0.5d + random.nextDouble() * 29.50d).setScale(2, RoundingMode.HALF_UP), "EUR"));
    }

    public Price priceByArticleIdAndCustomerId(String articleId, String customerId) {
        switch(customerId) {
            case "customer-1":
                return new Price(priceByArticleId(articleId).getValue().multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP), "EUR");
            case "customer-2":
                return new Price(priceByArticleId(articleId).getValue().multiply(new BigDecimal("0.85")).setScale(2, RoundingMode.HALF_UP), "EUR");
        }

        return Price.noPrice();
    }
}
