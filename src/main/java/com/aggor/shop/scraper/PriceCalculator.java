package com.aggor.shop.scraper;

import com.aggor.shop.model.Item;
import com.aggor.shop.model.Total;

import java.util.List;

public interface PriceCalculator {
    Total calculate(List<Item> items);
}
