package ru.job4j.concurrent;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    public synchronized String getContentWithPredicate(Predicate filter) {
        String output = "";
        try (InputStream input = new FileInputStream(file)) {
            int data;
            while ((data = input.read()) > 0) {
                if (filter.test(data)) {
                    output += (char) data;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output;
    }
}
