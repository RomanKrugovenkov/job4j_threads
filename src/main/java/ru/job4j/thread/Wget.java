package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File("tmp.xml");
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[512];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                var beforeAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                var afterAt = System.nanoTime();
                System.out.println("Read 512 bytes : " + (afterAt - beforeAt) + " nano.");
                var sleepTime =  1000000 * dataBuffer.length / (afterAt - beforeAt) / speed;
                System.out.println("Время задержки = " + sleepTime + " мс");
                Thread.sleep(sleepTime);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        Thread wget = new Thread(new Wget(url, speed));
        System.out.println("Загрузка файла с ограничением скорости " + speed + " кб/с...");
        wget.start();
        wget.join();
    }
}
