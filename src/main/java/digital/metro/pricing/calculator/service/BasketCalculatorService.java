package digital.metro.pricing.calculator.service;

import digital.metro.pricing.calculator.repository.PriceRepository;
import digital.metro.pricing.calculator.model.Basket;
import digital.metro.pricing.calculator.model.BasketCalculationResult;
import digital.metro.pricing.calculator.model.BasketEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BasketCalculatorService {

    private PriceRepository priceRepository;

    @Autowired
    public BasketCalculatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BasketCalculationResult calculateBasket(Basket basket) {
        Map<String, BigDecimal> pricedArticles = basket.getEntries().stream()
                .collect(Collectors.toMap(
                        BasketEntry::getArticleId,
                        entry -> calculateArticle(entry, basket.getCustomerId())));

        BigDecimal totalAmount = pricedArticles.values().stream()
                .reduce(BigDecimal.ONE, (a, b) -> a.add(b));

        return new BasketCalculationResult(basket.getCustomerId(), pricedArticles, totalAmount);
    }

    public BigDecimal calculateArticle(BasketEntry basketEntry, String customerId) {
        String articleId = basketEntry.getArticleId();

        if (customerId != null) {
            var customerPrice = priceRepository.priceByArticleIdAndCustomerId(articleId, customerId);
            if (customerPrice.isPresent()) {
                return customerPrice.get();
            }
        }
        return priceRepository.priceByArticleId(articleId);
    }

    public BigDecimal calculateArticle(BasketEntry basketEntry) {
        return calculateArticle(basketEntry, null);
    }
}
