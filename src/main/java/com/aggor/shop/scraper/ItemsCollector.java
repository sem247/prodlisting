package com.aggor.shop.scraper;

import com.aggor.shop.model.Item;
import com.aggor.shop.model.Items;
import com.aggor.shop.model.Total;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ItemsCollector {
    private final SiteScraper siteScraper;
    private final PriceCalculator priceCalculator;

    public ItemsCollector(SiteScraper siteScraper, PriceCalculator priceCalculator) {
        this.siteScraper = siteScraper;
        this.priceCalculator = priceCalculator;
    }

    public Items getItems(String url) throws IOException {
        final Document homePage = siteScraper.getPage(url);
        final List<String> productLinks = siteScraper.getProductLinks(homePage);
        final List<Item> results = productLinks.stream().map(p -> {
            try {
                final Document detailsPage = siteScraper.getPage(p);
                return new Item(
                        siteScraper.getTitle(detailsPage),
                        siteScraper.getKcalPer100g(detailsPage),
                        siteScraper.getUnitPrice(detailsPage),
                        siteScraper.getDescription(detailsPage)
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(toList());

        final Total total = priceCalculator.calculate(results);

        return new Items(results, total);
    }
}
