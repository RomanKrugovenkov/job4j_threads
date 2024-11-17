package ru.job4j.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotification {
    private ExecutorService pool;

    public EmailNotification(int numbersOfPool) {
        this.pool = Executors.newFixedThreadPool(numbersOfPool);
    }

    public void emailTo(User user) {
        String subject = String.format("Notification {%s} to email {%s}", user.userName, user.email);
        String body = String.format("Add a new event to {%s}", user.userName);
        Runnable task = () -> send(subject, body, user.email);
        pool.submit(task);
    }

    public void send(String subject, String body, String email) {
        System.out.println("send notification to " + email);
    }

    public void close() throws InterruptedException {
        pool.shutdown();
        while (!pool.isTerminated()) {
            Thread.sleep(100);
        }
        System.out.println("Pool shutdown completed.");
    }

    public static void main(String[] args) throws InterruptedException {
        EmailNotification emailNotification = new EmailNotification(2);
        User user1 = new User("Roma", "roma@j4j.com");
        User user2 = new User("Vova", "vova@j4j.com");
        emailNotification.emailTo(user1);
        emailNotification.emailTo(user2);
        emailNotification.close();
    }
}
