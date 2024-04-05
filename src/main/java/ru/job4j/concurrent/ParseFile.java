package ru.job4j.concurrent;

import java.io.*;
import java.util.function.Predicate;

public class ParseFile {
    private final File file;

    public ParseFile(File file) {
        this.file = file;
    }

    private String getContentWithPredicate(Predicate<Integer> filter) {
        StringBuilder output = new StringBuilder();
        try (InputStream input = new FileInputStream(file)) {
            int data;
            while ((data = input.read()) > 0) {
                if (filter.test(data)) {
                    output.append(data);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output.toString();
    }

    public synchronized String getContentWithoutFilter() {
        return getContentWithPredicate(f -> true);
    }

    public synchronized String getContentWithoutUnicode() {
        Predicate<Integer> filter = i -> i < 0x80;
        return getContentWithPredicate(filter);
    }
}
