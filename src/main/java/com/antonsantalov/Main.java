package com.antonsantalov;

import java.util.Random;

public class Main {
    private static final Counter COUNTER_BEAUTIFUL_3_LETTERS_TEXT = new Counter();
    private static final Counter COUNTER_BEAUTIFUL_4_LETTERS_TEXT = new Counter();
    private static final Counter COUNTER_BEAUTIFUL_5_LETTERS_TEXT = new Counter();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        ThreadGroup threadGroup = new ThreadGroup("thread group");

        final Thread thread1 = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    incrementNeededCounter(text);
                }
            }
        }, threadGroup.getName());
        thread1.start();

        final Thread thread2 = new Thread(() -> {
            for (String text : texts) {
                if (isIdenticalLetters(text)) {
                    incrementNeededCounter(text);
                }
            }
        }, threadGroup.getName());
        thread2.start();

        final Thread thread3 = new Thread(() -> {
            for (String text : texts) {
                if (isLettersInAscOrder(text)) {
                    incrementNeededCounter(text);
                }
            }
        }, threadGroup.getName());
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();

        threadGroup.interrupt();

        System.out.printf("Красивых слов с длиной %d: %d шт%n", 3, COUNTER_BEAUTIFUL_3_LETTERS_TEXT.getCounterValue());
        System.out.printf("Красивых слов с длиной %d: %d шт%n", 4, COUNTER_BEAUTIFUL_4_LETTERS_TEXT.getCounterValue());
        System.out.printf("Красивых слов с длиной %d: %d шт%n", 5, COUNTER_BEAUTIFUL_5_LETTERS_TEXT.getCounterValue());
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
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

    private static void incrementNeededCounter(String text) {
        switch (text.length()) {
            case 3 -> COUNTER_BEAUTIFUL_3_LETTERS_TEXT.incrementCounter();
            case 4 -> COUNTER_BEAUTIFUL_4_LETTERS_TEXT.incrementCounter();
            case 5 -> COUNTER_BEAUTIFUL_5_LETTERS_TEXT.incrementCounter();
        }
    }
}