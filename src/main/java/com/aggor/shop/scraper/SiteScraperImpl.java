package com.aggor.shop.scraper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.jsoup.Jsoup.connect;

public class SiteScraperImpl implements SiteScraper {
    static {
        final TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // Trust all
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // Trust all
                    }
                }
        };

        final HostnameVerifier trustAllHostnames = (hostname, session) -> true;

        try {
            System.setProperty("jsse.enableSNIExtension", "false");
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
        } catch (GeneralSecurityException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public Document getPage(String url) throws IOException {
        return connect(url).get();
    }

    @Override
    public List<String> getProductLinks(Document document) {
        final Elements productDetailsPages = document.select(".productInfo")
                .select("a[href]");

        return productDetailsPages.stream()
                .map(e -> e.select("a").first().attr("abs:href"))
                .collect(toList());
    }

    @Override
    public String getTitle(Document document) {
        return document.select(".productTitleDescriptionContainer")
                .select("h1")
                .first()
                .text();
    }

    @Override
    public Integer getKcalPer100g(Document document) {
        final Optional<Element> nutritionTable = Optional.ofNullable(document.select("table").first());

        final Optional<String> kcal = nutritionTable.map(t -> {
            final Optional<Element> kcalRow = t.select("tr").stream()
                    .filter(r -> r.text().contains("kcal"))
                    .findFirst();

            final Optional<Element> cell = kcalRow.map(r -> r.select("td").first());

            return cell.map(c -> {
                final String text = c.text();

                return text.contains("kcal") ? text.substring(0, text.indexOf("kcal")) : text;
            });
        }).flatMap(x -> x);

        return kcal.map(Integer::valueOf)
                .orElse(null);
    }

    @Override
    public BigDecimal getUnitPrice(Document document) {
        final String text = document.select(".pricePerUnit")
                .first().text();
        final String unitPrice = text.substring(1, text.indexOf("/unit"));

        return new BigDecimal(unitPrice);
    }

    @Override
    public String getDescription(Document document) {
        return document.select(".productText")
                .select("p")
                .first()
                .text();
    }
}
