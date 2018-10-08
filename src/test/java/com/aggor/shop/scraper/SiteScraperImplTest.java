package com.aggor.shop.scraper;

import com.aggor.shop.BaseTest;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.jsoup.Jsoup.connect;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class SiteScraperImplTest extends BaseTest {
    private final SiteScraperImpl sut = new SiteScraperImpl();

    private final String baseUrl = "https://localhost:8443";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().httpsPort(8443));

    @Test
    public void testGetPage() throws Exception {
        final String html = "<html>\n" +
                "    <body>Hello</body>\n" +
                "</html>";
        final Document expectedDocument = Jsoup.parse(html);

        final String simplePage = "/simple.html";
        final String fileContent = getFileContent("html/simple.html");

        wireMockRule.stubFor(
                get(urlEqualTo(simplePage))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(fileContent))
        );

        final Document document = sut.getPage(baseUrl + simplePage);

        wireMockRule.verify(1, getRequestedFor(urlEqualTo(simplePage)));
        assertThat(document.toString(), is(expectedDocument.toString()));
    }

    @Test
    public void testProductLinksSelection() throws Exception {
        final String listPage = "/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
        final String fileContent = getFileContent("html/product_listing_no_cross_sell_item_present.html");

        final Document document = getTestPage(listPage, fileContent);

        final List<String> expectedProductLinks = singletonList(baseUrl + detailsPage);

        final List<String> productLinks = sut.getProductLinks(document);

        assertThat(productLinks, is(expectedProductLinks));
    }

    @Test
    public void testProductLinksSelectionWhenItemHasCrossSellItem() throws Exception {
        final String listPage = "/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-blueberries-400g.html";
        final String fileContent = getFileContent("html/product_listing_with_cross_sell_item.html");

        final Document document = getTestPage(listPage, fileContent);

        final List<String> expectedProductLinks = singletonList(baseUrl + detailsPage);

        final List<String> productLinks = sut.getProductLinks(document);

        assertThat(productLinks, is(expectedProductLinks));
    }

    @Test
    public void testProductLinksSelectionWhenCrossSellItemsArePresent() throws Exception {
        final String listPage = "/serverside-test/site/www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";
        final String item1Page = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
        final String item2Page = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-blueberries-400g.html";
        final String fileContent = getFileContent("html/product_listing_with_cross_sell_item_present.html");

        final Document document = getTestPage(listPage, fileContent);

        final List<String> expectedProductLinks = Arrays.asList(baseUrl + item1Page, baseUrl + item2Page);

        final List<String> productLinks = sut.getProductLinks(document);

        assertThat(productLinks, is(expectedProductLinks));
    }

    @Test
    public void testGetProductTitle() throws Exception {
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
        final String fileContent = getFileContent("html/product_details.html");

        final Document document = getTestPage(detailsPage, fileContent);

        final String expectedTitle = "Sainsbury's Strawberries 400g";

        final String title = sut.getTitle(document);

        assertThat(title, is(expectedTitle));
    }

    @Test
    public void testGetProductKcalWhenPresentInVariant1() throws Exception {
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
        final String fileContent = getFileContent("html/product_details.html");

        final Document document = getTestPage(detailsPage, fileContent);

        final Integer expectedKcalPer100g = 33;

        final Integer kcalPer100g = sut.getKcalPer100g(document);

        assertThat(kcalPer100g, is(expectedKcalPer100g));
    }

    @Test
    public void testGetProductKcalWhenPresentInVariant2() throws Exception {
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-cherry-punnet-200g-468015-p-44.html";
        final String fileContent = getFileContent("html/product_details_variant_2.html");

        final Document document = getTestPage(detailsPage, fileContent);

        final Integer expectedKcalPer100g = 52;

        final Integer kcalPer100g = sut.getKcalPer100g(document);

        assertThat(kcalPer100g, is(expectedKcalPer100g));
    }

    @Test
    public void testGetProductKcalWhenNotPresent() throws Exception {
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-mixed-berries-300g.html";
        final String fileContent = getFileContent("html/product_details_with_no_nutrition_info.html");

        final Document document = getTestPage(detailsPage, fileContent);

        final Integer kcalPer100g = sut.getKcalPer100g(document);

        assertNull(kcalPer100g);
    }

    @Test
    public void testGetProductDescriptionForSingleLineDescription() throws Exception {
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
        final String fileContent = getFileContent("html/product_details.html");

        final Document document = getTestPage(detailsPage, fileContent);

        final String expectedDescription = "by Sainsbury's strawberries";

        final String description = sut.getDescription(document);

        assertThat(description, is(expectedDescription));
    }

    @Test
    public void testGetProductDescriptionForMultiLineDescription() throws Exception {
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-cherry-punnet-200g-468015-p-44.html";
        final String fileContent = getFileContent("html/product_details_with_multiline_description.html");

        final Document document = getTestPage(detailsPage, fileContent);

        final String expectedDescription = "Cherries";

        final String description = sut.getDescription(document);

        assertThat(description, is(expectedDescription));
    }

    @Test
    public void testGetProductUnitPrice() throws Exception {
        final String detailsPage = "/serverside-test/site/www.sainsburys.co.uk/shop/gb/groceries/berries-cherries-currants/sainsburys-british-strawberries-400g.html";
        final String fileContent = getFileContent("html/product_details.html");

        final Document document = getTestPage(detailsPage, fileContent);

        final BigDecimal expectedUnitPrice = new BigDecimal("1.75");

        final BigDecimal unitPrice = sut.getUnitPrice(document);

        assertThat(unitPrice, is(expectedUnitPrice));
    }

    private Document getTestPage(String link, String payload) throws IOException {
        wireMockRule.stubFor(
                get(urlEqualTo(link))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(payload))
        );

        return connect(baseUrl + link).get();
    }
}