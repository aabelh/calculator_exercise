package digital.metro.pricing.calculator.controller;

import digital.metro.pricing.calculator.model.Basket;
import digital.metro.pricing.calculator.model.BasketCalculationResult;
import digital.metro.pricing.calculator.model.BasketEntry;
import digital.metro.pricing.calculator.model.Price;
import digital.metro.pricing.calculator.service.BasketCalculatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 *
 * Rest controller responsible for calculating prices for basket or for article
 *
 */
@RestController()
@RequestMapping("/calculator")
public class CalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    private BasketCalculatorService basketCalculatorService;

    @Autowired
    public CalculatorController(BasketCalculatorService basketCalculatorService) {
        this.basketCalculatorService = basketCalculatorService;
    }

    @PostMapping("/calculate-basket")
    public BasketCalculationResult calculateBasket(@RequestBody Basket basket) {
        logger.info("action=calculator, step=calculateBasket");
        return basketCalculatorService.calculateBasket(basket);
    }

    @GetMapping("/article/{articleId}")
    public Price articlePrice(@PathVariable String articleId) {
        logger.info("action=calculator, step=calculatePriceArticle, articleId=%s", articleId);
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE));
    }

    @GetMapping("/article-price-for-customer")
    public Price articlePriceForCustomer(@RequestParam String articleId, @RequestParam String customerId) {
        logger.info("action=calculator, step=calculateArticle, articleId=%s, customerId=%s", articleId, customerId);
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), customerId);
    }
}
