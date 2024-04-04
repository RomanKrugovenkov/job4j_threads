package ru.job4j.concurrent;

import java.io.*;

public class SaveToFile {
    private final File file;

    public SaveToFile(File file) {
        this.file = file;
    }

    public synchronized void saveContent(String content) {
        try (OutputStream out = new FileOutputStream(file)) {
            for (int i = 0; i < content.length(); i++) {
                out.write(content.charAt(i));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
