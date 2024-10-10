package com.antonsantalov;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final AtomicInteger COUNT_BEAUTIFUL_3_LETTERS_TEXT = new AtomicInteger(0);
    private static final AtomicInteger COUNT_BEAUTIFUL_4_LETTERS_TEXT = new AtomicInteger(0);
    private static final AtomicInteger COUNT_BEAUTIFUL_5_LETTERS_TEXT = new AtomicInteger(0);
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);


        List<Thread> threads = new ArrayList<>();
        for (String text : texts) {
            Thread thread = new Thread(() -> {
                if (isBeautiful(text)) {
                    switch (text.length()) {
                        case 3 -> increment();
                        case 4 -> COUNT_BEAUTIFUL_4_LETTERS_TEXT.incrementAndGet();
                        case 5 -> COUNT_BEAUTIFUL_5_LETTERS_TEXT.incrementAndGet();
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }


        final List<Future<Void>> futures = new ArrayList<>();

        for (Future<Void> future : futures) {
            future.get();
        }

        threadPool.shutdown();


        System.out.printf("Красивых слов с длиной %d: %d шт%n", 3, COUNT_BEAUTIFUL_3_LETTERS_TEXT.get());
        System.out.printf("Красивых слов с длиной %d: %d шт%n", 4, COUNT_BEAUTIFUL_4_LETTERS_TEXT.get());
        System.out.printf("Красивых слов с длиной %d: %d шт%n", 5, COUNT_BEAUTIFUL_5_LETTERS_TEXT.get());
    }

    private static void increment() {
        COUNT_BEAUTIFUL_3_LETTERS_TEXT.incrementAndGet();
    }

    private static boolean isBeautiful(String text) {
        return isPalindrome(text) || isIdenticalLetters(text)
            || isLettersInAscOrder(text);
    }

    private static boolean isPalindrome(String text) {
        int leftLetter = 0;
        int rightLetter = text.length() - 1;
        while (leftLetter < rightLetter) {
            if (text.charAt(leftLetter) != text.charAt(rightLetter)) {
                return false;
            }
            leftLetter++;
            rightLetter--;
        }
        return true;
    }

    private static boolean isIdenticalLetters(String text) {
        char firstLetter = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != firstLetter) {
                return false;
            }
        }
        return true;
    }

    private static boolean isLettersInAscOrder(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}