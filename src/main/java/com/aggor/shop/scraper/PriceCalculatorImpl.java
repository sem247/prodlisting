package com.aggor.shop.scraper;

import com.aggor.shop.model.Item;
import com.aggor.shop.model.Total;

import java.math.BigDecimal;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

public class PriceCalculatorImpl implements PriceCalculator {
    private final BigDecimal divisor = new BigDecimal("1.20");

    @Override
    public Total calculate(List<Item> items) {
        final BigDecimal gross = items.stream()
                .map(Item::getUnitPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        final BigDecimal net = gross.divide(divisor, 2, ROUND_HALF_DOWN);

        final BigDecimal vat = gross.subtract(net);

        return new Total(gross, vat);
    }
}
