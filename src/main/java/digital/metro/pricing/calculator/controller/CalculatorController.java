package digital.metro.pricing.calculator.controller;

import digital.metro.pricing.calculator.model.Basket;
import digital.metro.pricing.calculator.model.BasketCalculationResult;
import digital.metro.pricing.calculator.model.BasketEntry;
import digital.metro.pricing.calculator.service.BasketCalculatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController()
@RequestMapping("/calculator")
public class CalculatorController {

    private BasketCalculatorService basketCalculatorService;

    @Autowired
    public CalculatorController(BasketCalculatorService basketCalculatorService) {
        this.basketCalculatorService = basketCalculatorService;
    }

    @PostMapping("/calculate-basket")
    public BasketCalculationResult calculateBasket(@RequestBody Basket basket) {
        return basketCalculatorService.calculateBasket(basket);
    }

    @GetMapping("/article/{articleId}")
    public BigDecimal articlePrice(@PathVariable String articleId) {
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE));
    }

    @GetMapping("/article-price-for-customer")
    public BigDecimal articlePriceForCustomer(@RequestParam String articleId, @RequestParam String customerId) {
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), customerId);
    }
}
