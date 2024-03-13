package digital.metro.pricing.calculator.service;

import digital.metro.pricing.calculator.model.Basket;
import digital.metro.pricing.calculator.model.BasketCalculationResult;
import digital.metro.pricing.calculator.model.BasketEntry;
import digital.metro.pricing.calculator.model.Price;
import digital.metro.pricing.calculator.repository.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Responsible for calculating price for the content of {@link Basket}
 *
 */
@Service
public class BasketCalculatorService {

    private static final Logger logger = LoggerFactory.getLogger(BasketCalculatorService.class);
    private PriceRepository priceRepository;

    @Autowired
    public BasketCalculatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    /**
     * Calculate the price of the content of the basket
     *
     * @param basket
     * @return {@link BasketCalculationResult}
     */
    public BasketCalculationResult calculateBasket(Basket basket) {
        logger.info("action=calculateBasket, step=start");
        Map<String, Price> pricedArticles = basket.getEntries().stream()
                .collect(Collectors.toMap(
                        BasketEntry::getArticleId,
                        entry -> calculateArticle(entry, basket.getCustomerId())));

        Price totalAmount = Price.createFromAddition(new ArrayList<>(pricedArticles.values()));
        logger.info("action=calculateBasket, step=totalPrice, customerId={}, amount={}, currencyCode={}",
                basket.getCustomerId(), totalAmount.getValue(), totalAmount.getCurrencyCode());
        return new BasketCalculationResult(basket.getCustomerId(), pricedArticles, totalAmount);
    }

    /**
     * Calculate price for {@link BasketEntry}  for an customerId
     *
     * @param basketEntry
     * @param customerId
     * @return {@link Price} for basketEntry and customerId
     */
    public Price calculateArticle(BasketEntry basketEntry, String customerId) {
        String articleId = basketEntry.getArticleId();
        if (customerId != null) {
            var customerPrice = priceRepository.priceByArticleIdAndCustomerId(articleId, customerId);
            if (customerPrice.isPriced()) {
                return totalPricePerItem(customerPrice, basketEntry);
            }
        }
        return totalPricePerItem(priceRepository.priceByArticleId(articleId), basketEntry);
    }

    public Price calculateArticle(BasketEntry basketEntry) {
        return calculateArticle(basketEntry, null);
    }

    private Price totalPricePerItem(Price pricePerItem, BasketEntry basketEntry) {
        Price price = pricePerItem.multiply(basketEntry.getQuantity());
        logger.info("action=calculateBasket, step=calculateArticle, articleId={}, price={}, quantity={}, totalPrice={}, currencyCode={}",
                basketEntry.getArticleId(), pricePerItem.getValue(), basketEntry.getQuantity(), price.getValue(), price.getCurrencyCode());
        return price;
    }
}
