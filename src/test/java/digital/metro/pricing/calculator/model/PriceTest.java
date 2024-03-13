package digital.metro.pricing.calculator.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by abelh at 13 March 2024
 */
class PriceTest {

    @Test
    void add_withSameCurrency_shouldSumValues() {
        // arrange
        var price1 = Price.from("12.00", "EUR");
        var price2 = Price.from("12.00", "EUR");

        // act
        var total = price1.add(price2);

        // assert
        assertThat(total.getValue()).isEqualTo(new BigDecimal(("24.00")));
        assertThat(total.getCurrencyCode()).isEqualTo("EUR");
    }

    @Test
    void add_withDifferentCurrency_shouldThrowIllegalArgumentException() {
        // arrange
        var price1 = Price.from("12.00", "EUR");
        var price2 = Price.from("12.00", "USD");

        // act & assert
        assertThrows(IllegalArgumentException.class, () ->price1.add(price2));
    }

    @Test
    void add_withNull_shouldThrowNullPointerException() {
        // arrange
        var price1 = Price.from("12.00", "EUR");
        Price price2 = null;

        // act & assert
        assertThrows(NullPointerException.class, () -> price1.add(price2));
    }


    @Test
    void createFromAddition_withSameCurrencies_shouldCalculateTotal() {
        // arrange
        var p1 = Price.from("1.00", "EUR");
        var p2 = Price.from("2.00", "EUR");
        var listOfPrices = List.of(p1, p2);

        // act
        var result = Price.createFromAddition(listOfPrices);

        // assert
        assertThat(result.getValue()).isEqualTo(new BigDecimal(3).setScale(2));
        assertThat(result.getCurrencyCode()).isEqualTo("EUR");
    }

    @Test
    void createFromAddition_witDifferentCurrencies_shouldThrowIllegalArgumentException() {
        // arrange
        var p1 = Price.from("1.00", "EUR");
        var p2 = Price.from("2.00", "USD");
        var listOfPrices = List.of(p1, p2);

        // act
        assertThrows(IllegalArgumentException.class, () ->Price.createFromAddition(listOfPrices));
    }
}