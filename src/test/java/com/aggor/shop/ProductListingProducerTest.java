package com.aggor.shop;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProductListingProducerTest extends BaseTest {
    private final ProductListingProducer sut = new ProductListingProducer();

    @Test
    public void testProductItemsAsJsonForTheTestSainsburyGrocerySite() throws Exception {
        final String expectedJson = getExpectationJson("json/expected_items.json");

        final String itemsAsJson = sut.getItemsAsJson();

        assertThat(itemsAsJson, is(expectedJson));
    }
}