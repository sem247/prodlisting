package com.aggor.shop.json;

import com.aggor.shop.BaseTest;
import com.aggor.shop.model.Item;
import com.aggor.shop.model.Items;
import com.aggor.shop.model.Total;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class JsonfierTest extends BaseTest {
    private final Jsonfier sut = JsonfierProvider.provideJsonfier();
    private final JsonParser jsonParser = new JsonParser();
    private final Gson gson = new Gson();

    @Test
    public void testJsonfyItemsWhenResultsIsEmpty() throws Exception {
        final String expectedJson = getExpectation("json/empty_results.json");

        final Total total = new Total(new BigDecimal("0.00"), new BigDecimal("0.00"));
        final Items items = new Items(emptyList(), total);

        final String json = sut.toJson(items);

        assertThat(json, is(expectedJson));
    }

    @Test
    public void testJsonfyItemsWhenResultsHasSingleItem() throws Exception {
        final String expectedJson = getExpectation("json/single_item.json");

        final List<Item> listing = singletonList(
                new Item("Sainsbury's Strawberries 400g", 33, new BigDecimal("1.75"), "by Sainsbury's strawberries")
        );
        final Total total = new Total(new BigDecimal("1.75"), new BigDecimal("0.29"));
        final Items items = new Items(listing, total);

        final String json = sut.toJson(items);

        assertThat(json, is(expectedJson));
    }

    @Test
    public void testJsonfyItemsWhenResultsHasMultipleItems() throws Exception {
        final String expectedJson = getExpectation("json/multiple_items.json");

        final List<Item> listing = asList(
                new Item("Sainsbury's Strawberries 400g", 33, new BigDecimal("1.75"), "by Sainsbury's strawberries"),
                new Item("Sainsbury's Blueberries 200g", 45, new BigDecimal("1.75"), "by Sainsbury's blueberries"),
                new Item("Sainsbury's Cherry Punnet 200g", 52, new BigDecimal("1.5"), "Cherries")
        );
        final Total total = new Total(new BigDecimal("5.00"), new BigDecimal("0.83"));
        final Items items = new Items(listing, total);

        final String json = sut.toJson(items);

        assertThat(json, is(expectedJson));
    }

    @Test
    public void testJsonfyItemsWhenItemHasNoKcal() throws Exception {
        final String expectedJson = getExpectation("json/no_kcal_item.json");

        final List<Item> listing = singletonList(
                new Item("Sainsbury's Strawberries 400g", null, new BigDecimal("1.75"), "by Sainsbury's strawberries")
        );
        final Total total = new Total(new BigDecimal("1.75"), new BigDecimal("0.29"));
        final Items items = new Items(listing, total);

        final String json = sut.toJson(items);

        assertThat(json, is(expectedJson));
    }

    private String getExpectation(String file) throws Exception {
        final String fileContent = getFileContent(file);

        final JsonElement jsonElement = jsonParser.parse(fileContent);

        return gson.toJson(jsonElement);
    }
}