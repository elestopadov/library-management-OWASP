package com.example.library.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReaderTest {
    @Test
    void shouldCreateReader() {
        Reader reader = new Reader("reader-1", "Alice Example", "alice@example.com");

        assertThat(reader.getId()).isEqualTo("reader-1");
        assertThat(reader.getName()).isEqualTo("Alice Example");
        assertThat(reader.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void shouldRejectInvalidEmail() {
        assertThatThrownBy(() -> new Reader("reader-1", "Alice", "not-an-email"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email has invalid format");
        assertThatThrownBy(() -> new Reader("reader-1", "Alice", "alice @example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("email has invalid format");
    }

    @Test
    void shouldRejectNullAndBlankRequiredFields() {
        assertThatThrownBy(() -> new Reader(null, "Alice", "alice@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Reader(" ", "Alice", "alice@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Reader("reader-1", null, "alice@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Reader("reader-1", "", "alice@example.com"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Reader("reader-1", "Alice", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Reader("reader-1", "Alice", ""))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldUseIdForEqualityAndHashCode() {
        Reader first = new Reader("reader-1", "Alice", "alice@example.com");
        Reader second = new Reader("reader-1", "Bob", "bob@example.com");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("reader-1");
    }

    @Test
    void shouldContainUsefulValuesInToString() {
        Reader reader = new Reader("reader-1", "Alice", "alice@example.com");

        assertThat(reader.toString())
                .contains("reader-1", "Alice")
                .doesNotContain("alice@example.com");
    }
}
