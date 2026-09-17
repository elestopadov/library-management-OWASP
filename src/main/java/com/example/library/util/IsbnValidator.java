package com.example.library.util;

/**
 * Validates ISBN-10 and ISBN-13 values after normalizing spaces and hyphens.
 */
public final class IsbnValidator {
    /**
     * Creates an ISBN validator.
     */
    public IsbnValidator() {
    }

    /**
     * Checks whether the supplied value is a valid ISBN-10 or ISBN-13.
     *
     * @param isbn value to validate
     * @return {@code true} when the value is valid
     */
    public boolean isValid(String isbn) {
        String normalized = normalize(isbn);
        return isValidIsbn10(normalized) || isValidIsbn13(normalized);
    }

    /**
     * Checks the ISBN-10 checksum, allowing {@code X} as the final check digit.
     *
     * @param isbn value to validate
     * @return {@code true} when the value is a valid ISBN-10
     */
    public boolean isValidIsbn10(String isbn) {
        String normalized = normalize(isbn);
        if (normalized == null || normalized.length() != 10) {
            return false;
        }

        for (int index = 0; index < 9; index++) {
            if (!Character.isDigit(normalized.charAt(index))) {
                return false;
            }
        }

        char checkDigit = Character.toUpperCase(normalized.charAt(9));
        if (!(Character.isDigit(checkDigit) || checkDigit == 'X')) {
            return false;
        }

        int sum = 0;
        for (int index = 0; index < 10; index++) {
            int value = index == 9 && checkDigit == 'X'
                    ? 10
                    : Character.digit(normalized.charAt(index), 10);
            sum += value * (10 - index);
        }
        return sum % 11 == 0;
    }

    /**
     * Checks the ISBN-13 checksum.
     *
     * @param isbn value to validate
     * @return {@code true} when the value is a valid ISBN-13
     */
    public boolean isValidIsbn13(String isbn) {
        String normalized = normalize(isbn);
        if (normalized == null || normalized.length() != 13) {
            return false;
        }

        for (char character : normalized.toCharArray()) {
            if (!Character.isDigit(character)) {
                return false;
            }
        }

        int sum = 0;
        for (int index = 0; index < 12; index++) {
            int value = Character.digit(normalized.charAt(index), 10);
            sum += value * (index % 2 == 0 ? 1 : 3);
        }
        int expectedCheckDigit = (10 - (sum % 10)) % 10;
        int actualCheckDigit = Character.digit(normalized.charAt(12), 10);
        return expectedCheckDigit == actualCheckDigit;
    }

    private String normalize(String isbn) {
        if (isbn == null) {
            return null;
        }
        return isbn.replace("-", "").replace(" ", "");
    }
}
