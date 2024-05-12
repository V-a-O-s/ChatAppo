package ch.jchat.chatapp.misc;

import java.util.Random;

public class RandomGenerator {
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(LETTERS.length());
            result.append(LETTERS.charAt(randomIndex));
        }
        return result.toString();
    }
}
