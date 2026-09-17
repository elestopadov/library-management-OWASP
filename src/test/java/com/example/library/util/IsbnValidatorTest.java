package com.example.library.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IsbnValidatorTest {
    private final IsbnValidator validator = new IsbnValidator();

    @Test
    void shouldValidateIsbn10() {
        assertThat(validator.isValidIsbn10("0-306-40615-2")).isTrue();
    }

    @Test
    void shouldValidateIsbn10WithXCheckDigit() {
        assertThat(validator.isValidIsbn10("0-8044-2957-X")).isTrue();
        assertThat(validator.isValidIsbn10("0-8044-2957-x")).isTrue();
    }

    @Test
    void shouldValidateIsbn13() {
        assertThat(validator.isValidIsbn13("978-0-306-40615-7")).isTrue();
    }

    @Test
    void shouldRejectInvalidChecksum() {
        assertThat(validator.isValid("978-0-306-40615-8")).isFalse();
        assertThat(validator.isValidIsbn10("0-306-40615-3")).isFalse();
    }

    @Test
    void shouldRejectNullAndBlankValues() {
        assertThat(validator.isValid(null)).isFalse();
        assertThat(validator.isValid("   ")).isFalse();
        assertThat(validator.isValid("")).isFalse();
    }

    @Test
    void shouldRejectWrongLengthValues() {
        assertThat(validator.isValid("123456789")).isFalse();
        assertThat(validator.isValid("123456789012345")).isFalse();
    }

    @Test
    void shouldRejectLettersOutsideIsbn10CheckDigit() {
        assertThat(validator.isValidIsbn10("12345ABCDE9")).isFalse();
        assertThat(validator.isValidIsbn10("123456789A")).isFalse();
    }

    @Test
    void shouldRejectLettersInIsbn13() {
        assertThat(validator.isValidIsbn13("978030640615X")).isFalse();
    }

    @Test
    void shouldNormalizeSpacesAndHyphens() {
        assertThat(validator.isValid("978 0 306 40615 7")).isTrue();
        assertThat(validator.isValid("978-0-306-40615-7")).isTrue();
    }
}
