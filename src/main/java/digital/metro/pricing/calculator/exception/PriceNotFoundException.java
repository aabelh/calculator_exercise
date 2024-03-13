package digital.metro.pricing.calculator.exception;

/**
 * Exception thrown when a price is not found in data base
 *
 * Created by abelh at 13 March 2024
 */
public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String message) {
        super(message);
    }
}
