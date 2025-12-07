package com.gadgetry.util;

public class StringNormalizationUtil {

    private StringNormalizationUtil() {
        // prevent instantiation for util class
    }

    public static String normalize(String value) {
        if (value == null) {
            return null;
        }
        return value.trim().toLowerCase();
    }
}
