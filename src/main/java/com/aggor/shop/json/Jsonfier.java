package com.aggor.shop.json;

import com.aggor.shop.model.Items;
import com.google.gson.Gson;

public class Jsonfier {
    private final Gson gson;

    public Jsonfier(Gson gson) {
        this.gson = gson;
    }

    public String toJson(Items items) {
        return gson.toJson(items);
    }
}
