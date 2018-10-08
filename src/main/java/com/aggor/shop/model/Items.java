package com.aggor.shop.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Items {
    private final List<Item> results = new LinkedList<>();
    private final Total total;

    public Items(List<Item> results, Total total) {
        this.results.addAll(results);
        this.total = total;
    }

    public List<Item> getResults() {
        return results;
    }

    public Total getTotal() {
        return total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Items tree = (Items) o;
        return Objects.equals(results, tree.results) &&
                Objects.equals(total, tree.total);
    }

    @Override
    public int hashCode() {

        return Objects.hash(results, total);
    }

    @Override
    public String toString() {
        return "Tree{" +
                "results=" + results +
                ", total=" + total +
                '}';
    }
}