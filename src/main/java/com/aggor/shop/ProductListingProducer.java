package com.aggor.shop;

import com.aggor.shop.json.Jsonfier;
import com.aggor.shop.json.JsonfierProvider;
import com.aggor.shop.model.Items;
import com.aggor.shop.scraper.ItemsCollector;
import com.aggor.shop.scraper.ItemsCollectorProvider;

import java.io.IOException;

public class ProductListingProducer {
    public String getItemsAsJson() throws IOException {
        final String homePage = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        final ItemsCollector itemsCollector = ItemsCollectorProvider.provideItemsCollector();
        final Jsonfier jsonfier = JsonfierProvider.provideJsonfier();

        final Items items = itemsCollector.getItems(homePage);

        return jsonfier.toJson(items);
    }

    public static void main(String[] args) throws Exception {
        final ProductListingProducer productListingProducer = new ProductListingProducer();
        final String json = productListingProducer.getItemsAsJson();

        System.out.println(json);
    }
}
