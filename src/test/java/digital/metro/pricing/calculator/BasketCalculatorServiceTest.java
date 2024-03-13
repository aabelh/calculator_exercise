package digital.metro.pricing.calculator;

import digital.metro.pricing.calculator.model.Basket;
import digital.metro.pricing.calculator.model.BasketCalculationResult;
import digital.metro.pricing.calculator.model.BasketEntry;
import digital.metro.pricing.calculator.model.Price;
import digital.metro.pricing.calculator.repository.PriceRepository;
import digital.metro.pricing.calculator.service.BasketCalculatorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BasketCalculatorServiceTest {

    @Mock
    private PriceRepository mockPriceRepository;

    private BasketCalculatorService basketCalculatorService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        basketCalculatorService = new BasketCalculatorService(mockPriceRepository);
    }

    @Test
    public void calculateArticle_withBasketEntryAndNoCustomerId_shouldCalculatePriceCorrect() {
        // arrange
        String articleId = "article-1";
        var price = Price.from("34.29", "EUR");
        var basketEntry = new BasketEntry(articleId, BigDecimal.ONE);
        var expectedPrice = price.multiply(basketEntry.getQuantity());
        Mockito.when(mockPriceRepository.priceByArticleId(articleId)).thenReturn(price);

        // act
        Price result = basketCalculatorService.calculateArticle(basketEntry);

        // assert
        Assertions.assertThat(result).isEqualTo(expectedPrice);
    }

    @Test
    public void calculateArticle_withQuantityGraterThenOne_shouldCalculatePriceCorrect() {
        // arrange
        String articleId = "article-1";
        Price customerPrice = Price.from("29.99", "EUR");
        String customerId = "customer-1";
        var basketEntry = new BasketEntry(articleId, BigDecimal.TEN);
        var expectedPrice = customerPrice.multiply(basketEntry.getQuantity());
        Mockito.when(mockPriceRepository.priceByArticleIdAndCustomerId(articleId, customerId)).thenReturn(customerPrice);

        // act
        Price result = basketCalculatorService.calculateArticle(basketEntry, customerId);

        // assert
        Assertions.assertThat(result).isEqualTo(expectedPrice);
    }

    @Test
    public void calculateBasket_withMultipleItemsAndVariousQuantity_shouldCalculateTotalAmountCorrect() {
        // arrange
        Basket basket = new Basket("customer-1", Set.of(
                new BasketEntry("article-1", BigDecimal.TEN),
                new BasketEntry("article-2", BigDecimal.ONE),
                new BasketEntry("article-3", BigDecimal.TEN)));

        Map<String, Price> prices = Map.of(
                "article-1", Price.from("1.50", "EUR"),
                "article-2", Price.from("0.29", "EUR"),
                "article-3", Price.from("9.99", "EUR")
        );

        Map<String, Price> expectedPrices = basket.getEntries().stream()
                .collect(Collectors.toMap(
                                BasketEntry::getArticleId,
                                e -> prices.get(e.getArticleId()).multiply(e.getQuantity())
                        )
                );
        Price expectedTotalAmount = Price.createFromAddition(new ArrayList<>(expectedPrices.values()));

        Mockito.when(mockPriceRepository.priceByArticleIdAndCustomerId("article-1", "customer-1"))
                .thenReturn(prices.get("article-1"));
        Mockito.when(mockPriceRepository.priceByArticleIdAndCustomerId("article-2", "customer-1"))
                .thenReturn(prices.get("article-2"));
        Mockito.when(mockPriceRepository.priceByArticleIdAndCustomerId("article-3", "customer-1"))
                .thenReturn(prices.get("article-3"));

        // act
        BasketCalculationResult result = basketCalculatorService.calculateBasket(basket);

        // assert
        Assertions.assertThat(result.getCustomerId()).isEqualTo(basket.getCustomerId());
        Assertions.assertThat(result.getPricedBasketEntries()).isEqualTo(expectedPrices);
        Assertions.assertThat(result.getTotalAmount()).isEqualTo(expectedTotalAmount);
    }
}
