package com.aggor.shop.scraper;

import com.aggor.shop.model.Item;
import com.aggor.shop.model.Items;
import com.aggor.shop.model.Total;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class ItemsCollectorTest {
    private final SiteScraper siteScraper = mock(SiteScraper.class);
    private final PriceCalculator priceCalculator = mock(PriceCalculator.class);
    private final ItemsCollector sut = new ItemsCollector(siteScraper, priceCalculator);

    @Test
    public void testCollectItems() throws Exception {
        final String url = "http://site";
        final String details1 = "http://details1";
        final String details2 = "http://details2";
        final Document document = Jsoup.parse("<html></html>");
        final String title1 = "title1";
        final String title2 = "title2";
        final Integer kcal1 = 2;
        final Integer kcal2 = 3;
        final BigDecimal unitPrice1 = new BigDecimal("1.20");
        final BigDecimal unitPrice2 = new BigDecimal("1.35");
        final String desc1 = "Description 1";
        final String desc2 = "Description 2";
        final Total total = new Total(new BigDecimal("1.00"), new BigDecimal("0.01"));
        final List<Item> results = asList(new Item(title1, kcal1, unitPrice1, desc1),
                new Item(title2, kcal2, unitPrice2, desc2));
        final Items expectedItems = new Items(results, total);

        when(siteScraper.getPage(url)).thenReturn(document);
        when(siteScraper.getPage(details1)).thenReturn(document);
        when(siteScraper.getPage(details2)).thenReturn(document);
        when(siteScraper.getProductLinks(document)).thenReturn(asList(details1, details2));
        when(siteScraper.getTitle(any(Document.class))).thenReturn(title1).thenReturn(title2);
        when(siteScraper.getKcalPer100g(any(Document.class))).thenReturn(kcal1).thenReturn(kcal2);
        when(siteScraper.getUnitPrice(any(Document.class))).thenReturn(unitPrice1).thenReturn(unitPrice2);
        when(siteScraper.getDescription(any(Document.class))).thenReturn(desc1).thenReturn(desc2);
        when(priceCalculator.calculate(anyList())).thenReturn(total);

        final Items items = sut.getItems(url);

        verify(siteScraper, times(1)).getPage(url);
        verify(siteScraper, times(1)).getPage(details1);
        verify(siteScraper, times(1)).getPage(details2);
        verify(siteScraper, times(1)).getProductLinks(document);
        verify(siteScraper, times(2)).getTitle(any(Document.class));
        verify(siteScraper, times(2)).getKcalPer100g(any(Document.class));
        verify(siteScraper, times(2)).getUnitPrice(any(Document.class));
        verify(siteScraper, times(2)).getDescription(any(Document.class));
        verify(priceCalculator, times(1)).calculate(anyList());

        assertThat(items, is(expectedItems));
    }

    @Test(expected = IOException.class)
    public void testThrowException() throws Exception {
        final String url = "http://site";

        when(siteScraper.getPage(url)).thenThrow(new IOException());

        sut.getItems(url);
    }
}