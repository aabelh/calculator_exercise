package digital.metro.pricing.calculator.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * A dummy implementation for testing purposes. In production, we would get real prices from a database.
 */
@Repository
public class PriceRepository {

    private Map<String, BigDecimal> prices = new HashMap<>();
    private Random random = new Random();

    public BigDecimal priceByArticleId(String articleId) {
        return prices.computeIfAbsent(articleId,
                key -> BigDecimal.valueOf(0.5d + random.nextDouble() * 29.50d).setScale(2, RoundingMode.HALF_UP));
    }

    public Optional<BigDecimal> priceByArticleIdAndCustomerId(String articleId, String customerId) {
        switch(customerId) {
            case "customer-1":
                return Optional.of(priceByArticleId(articleId).multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP));
            case "customer-2":
                return Optional.of(priceByArticleId(articleId).multiply(new BigDecimal("0.85")).setScale(2, RoundingMode.HALF_UP));
        }

        return Optional.empty();
    }
}
