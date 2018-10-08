package com.aggor.shop.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonfierProvider {
    public static Jsonfier provideJsonfier() {
        final Gson gson = new GsonBuilder().create();

        return new Jsonfier(gson);
    }
}
