package digital.metro.pricing.calculator.model;

import com.google.common.base.Preconditions;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Representation of price object and possible operations with prices
 *
 * Created by aabelh at 12 March 2024
 */
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString()
public class Price {

    private static final Price NO_PRICE = new Price(null, null);

    private BigDecimal value;
    private String currencyCode;

    /**
     * Return a 'no price' object, which has both value and currency code null
     *
     * @return noPrice object
     */
    public static Price noPrice() {
        return NO_PRICE;
    }

    /**
     * Create new price using String {@param value} and String {@param currencyCode}
     *
     * Throws:
     * NumberFormatException – if val is not a valid representation of a BigDecimal.
     *
     * @param value numeric value representing the value of price
     * @param currencyCode currency code associated with the price
     * @return new price
     */
    public static Price from(String value, String currencyCode) {
        return new Price(new BigDecimal(value), currencyCode);
    }

    /**
     * Return new price by adding all values from {@param addends}
     * only if {@param addends}  are not null and all currencyCodes has same value
     *
     * If addends is an empty list 'noPrice' object will be returned
     *
     * Throws:
     * IllegalArgumentException - if one currency is different by others
     * NullPointerException - if addends is null
     *
     * @param addends {@code List<Price>} prices to be sum
     * @return new price with value equals to sum of all addends and same currency
     */
    public static Price createFromAddition(List<Price> addends) {
        Preconditions.checkNotNull(addends, "Null addends");

        if(addends.isEmpty()) {
            return noPrice();
        }

        var sampleAddend = addends.get(0);
        var zeroPrice = new Price(BigDecimal.ZERO, sampleAddend.getCurrencyCode());

        return addends.stream().reduce(zeroPrice, Price::add);
    }

    /**
     * return true if price is other than noPrice object
     * @return true if current object is not noPrice false is current object is noPrice
     */
    public boolean isPriced() {
        return !this.equals(NO_PRICE);
    }

    /**
     * Return new price object by adding current value and {@param other} value only if currency is the same and other is not null
     *
     * Throws:
     * NullPointerException - if other is null
     * IllegalArgumentException - if currencyCode are not equals
     *
     * @param other price to be added to the current price
     * @return new price with sum of values and same currency
     */
    public Price add(Price other) {
        Preconditions.checkNotNull(other, "Null addendum");
        Preconditions.checkArgument(this.currencyCode.equalsIgnoreCase(other.getCurrencyCode()), "Currency codes are not equals [" + currencyCode + ", " + other.getCurrencyCode() +"]");

        return new Price(this.getValue().add(other.getValue()), this.getCurrencyCode());
    }

    /**
     * Return new price with value multiplied by {@param factor} only if {@param factor} is not null and not zero
     *
     * Throws:
     * NullPointerException - if other is null
     * IllegalArgumentException - if currencyCode are not equals
     *
     * @param factor multiplication factor
     * @return new price with value multiplied by {@param factor} and same currency
     */
    public Price multiply(BigDecimal factor) {
        Preconditions.checkNotNull(factor);
        Preconditions.checkArgument(!BigDecimal.ZERO.equals(factor));
        return new Price(this.getValue().multiply(factor), this.getCurrencyCode());
    }
}
