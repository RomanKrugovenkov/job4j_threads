package ru.job4j.thread;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

public class Wget implements Runnable {
    private final String url;
    private final String fileName;
    private final int speed;

    public Wget(String url, String fileName, int speed) {
        this.url = url;
        this.fileName = fileName;
        this.speed = speed;
    }

    @Override
    public void run() {
        try (var input = new URL(url).openStream(); var output = new FileOutputStream(fileName)) {
            var dataBuffer = new byte[512];
            var startAt = System.currentTimeMillis();
            long download = 0;
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                download += bytesRead;
                if (download > speed) {
                    long diffAt = System.currentTimeMillis() - startAt;
                    if (diffAt > 1000) {
                        var sleepTime = 1000000 * download / diffAt / speed;
                        Thread.sleep(sleepTime);
                        System.out.println("Время задержки = " + sleepTime + " мс");
                    }
                }
                output.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void validation(String[] args) {
        boolean urlTrue;
        try {
            new URL(args[0]).toURI();
            urlTrue = true;
        } catch (MalformedURLException e) {
            urlTrue = false;
        } catch (URISyntaxException e) {
            urlTrue = false;
        }
        if (!urlTrue) {
            throw new IllegalArgumentException(String.format("URL is incorrect \"%s\"", Paths.get(args[0]).getFileName()));
        }
        if (Integer.parseInt(args[1]) < 1) {
            throw new IllegalArgumentException(String.format("Speed is incorrect \"%s\"", args[1]));
        }
    }

    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        validation(args);
        String url = args[0];
        String fileName = Paths.get(new URL(url).getPath()).getFileName().toString();
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, fileName, speed));
        System.out.println("Загрузка файла с ограничением скорости " + speed + " кб/с...");
        wget.start();
        wget.join();
    }
}
