package com.aggor.shop.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Objects;

public class Item {
    private final String title;
    @SerializedName("kcal_per_100g")
    private final Integer kcalPer100g;
    @SerializedName("unit_price")
    private final BigDecimal unitPrice;
    private final String description;

    public Item(String title, Integer kcalPer100g, BigDecimal unitPrice, String description) {
        this.title = title;
        this.kcalPer100g = kcalPer100g;
        this.unitPrice = unitPrice;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public Integer getKcalPer100g() {
        return kcalPer100g;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(title, item.title) &&
                Objects.equals(kcalPer100g, item.kcalPer100g) &&
                Objects.equals(unitPrice, item.unitPrice) &&
                Objects.equals(description, item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, kcalPer100g, unitPrice, description);
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", kcalPer100g=" + kcalPer100g +
                ", unitPrice=" + unitPrice +
                ", description='" + description + '\'' +
                '}';
    }
}
