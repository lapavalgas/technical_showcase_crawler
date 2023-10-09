package com.challenge.backend.helper;

import com.challenge.backend.utils.CrawlConstants;

import java.util.Random;

public class IdGeneratorHelper {
    private static final int ID_LENGTH = 8;
    private static final Random RANDOM = new Random();

    public static String generateId() {
        final StringBuilder id = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            int index = RANDOM.nextInt(CrawlConstants.RegexPatterns.ALPHANUMERIC.length());
            id.append(CrawlConstants.RegexPatterns.ALPHANUMERIC.charAt(index));
        }
        return id.toString();
    }
}
