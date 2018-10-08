package com.aggor.shop;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseTest {
    protected String getFileContent(String file) throws Exception {
        final URI fileLocation = this.getClass().getResource("/" + file).toURI();

        final Path path = Paths.get(fileLocation);

        return new String(Files.readAllBytes(path));
    }
}
