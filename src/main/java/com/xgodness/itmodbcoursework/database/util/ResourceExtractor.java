package com.xgodness.itmodbcoursework.database.util;

import java.io.InputStream;
import java.util.Scanner;

public class ResourceExtractor {
    public String readResourceAsString(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IllegalArgumentException("File %s was not found".formatted(filename));
        } else {
            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
            return scanner.hasNext() ? scanner.next() : "";
        }
    }

    public InputStream readResourceAsInputStream(String filename) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(filename);
    }
}