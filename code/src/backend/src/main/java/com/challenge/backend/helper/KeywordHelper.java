package com.challenge.backend.helper;

public class KeywordHelper {
    private static final int MIN_LENGTH = 4;
    private static final int MAX_LENGTH = 32;

    public static boolean isValid(String keyword) {
        if (keyword == null) {
            return false;
        }
        int length = keyword.length();
        return length >= MIN_LENGTH && length <= MAX_LENGTH;
    }
}