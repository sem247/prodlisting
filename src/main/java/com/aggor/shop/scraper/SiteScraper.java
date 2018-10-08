package com.aggor.shop.scraper;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface SiteScraper {
    Document getPage(String url) throws IOException;

    List<String> getProductLinks(Document document);

    String getTitle(Document document);

    Integer getKcalPer100g(Document document);

    BigDecimal getUnitPrice(Document document);

    String getDescription(Document document);
}
