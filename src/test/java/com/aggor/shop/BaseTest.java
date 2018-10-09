package com.aggor.shop;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseTest {
    private final JsonParser jsonParser = new JsonParser();
    private final Gson gson = new Gson();

    protected String getFileContent(String file) throws Exception {
        final URI fileLocation = this.getClass().getResource("/" + file).toURI();

        final Path path = Paths.get(fileLocation);

        return new String(Files.readAllBytes(path));
    }

    protected String getExpectationJson(String file) throws Exception {
        final String fileContent = getFileContent(file);

        final JsonElement jsonElement = jsonParser.parse(fileContent);

        return gson.toJson(jsonElement);
    }
}
