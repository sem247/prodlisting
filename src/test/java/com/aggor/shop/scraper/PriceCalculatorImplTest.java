package com.aggor.shop.scraper;

import com.aggor.shop.model.Item;
import com.aggor.shop.model.Total;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PriceCalculatorImplTest {
    private final PriceCalculatorImpl sut = new PriceCalculatorImpl();

    @Test
    public void testCalculateTotal() {
        final Total expectedTotal = new Total(new BigDecimal("5.00"), new BigDecimal("0.83"));

        final List<Item> items = asList(
                new Item("Sainsbury's Strawberries 400g", 33, new BigDecimal("1.75"), "by Sainsbury's strawberries"),
                new Item("Sainsbury's Blueberries 200g", 45, new BigDecimal("1.75"), "by Sainsbury's blueberries"),
                new Item("Sainsbury's Cherry Punnet 200g", 52, new BigDecimal("1.50"), "Cherries")
        );

        final Total total = sut.calculate(items);

        assertThat(total, is(expectedTotal));
    }
}