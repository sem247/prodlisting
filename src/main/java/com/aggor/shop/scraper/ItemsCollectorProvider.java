package com.aggor.shop.scraper;

public class ItemsCollectorProvider {
    public static ItemsCollector provideItemsCollector() {
        final SiteScraper siteScraper = new SiteScraperImpl();
        final PriceCalculator priceCalculator = new PriceCalculatorImpl();

        return new ItemsCollector(siteScraper, priceCalculator);
    }
}
