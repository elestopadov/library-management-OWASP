package com.example.library.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookTest {
    @Test
    void shouldCreateAvailableBookByDefault() {
        Book book = new Book("book-1", "Clean Code", "Robert C. Martin", "9780132350884");

        assertThat(book.getId()).isEqualTo("book-1");
        assertThat(book.getTitle()).isEqualTo("Clean Code");
        assertThat(book.getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(book.getIsbn()).isEqualTo("9780132350884");
        assertThat(book.isAvailable()).isTrue();
    }

    @Test
    void shouldCreateBookWithExplicitAvailability() {
        Book book = new Book("book-1", "Title", "Author", "123456789X", false);

        assertThat(book.isAvailable()).isFalse();
    }

    @Test
    void shouldUseIdForEqualityAndHashCode() {
        Book first = new Book("book-1", "Title A", "Author A", "1111111111");
        Book second = new Book("book-1", "Title B", "Author B", "2222222222", false);

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("book-1");
    }

    @Test
    void shouldContainUsefulValuesInToString() {
        Book book = new Book("book-1", "Clean Code", "Robert C. Martin", "9780132350884");

        assertThat(book.toString())
                .contains("book-1", "Clean Code", "Robert C. Martin", "9780132350884", "available=true");
    }

    @Test
    void shouldChangeAvailabilityThroughDomainMethods() {
        Book book = new Book("book-1", "Clean Code", "Robert C. Martin", "9780132350884");

        book.markUnavailable();
        assertThat(book.isAvailable()).isFalse();

        book.markAvailable();
        assertThat(book.isAvailable()).isTrue();
    }

    @Test
    void shouldRejectNullAndBlankRequiredFields() {
        assertThatThrownBy(() -> new Book(null, "Title", "Author", "123"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Book(" ", "Title", "Author", "123"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Book("id", null, "Author", "123"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Book("id", "", "Author", "123"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Book("id", "Title", null, "123"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Book("id", "Title", " ", "123"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Book("id", "Title", "Author", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Book("id", "Title", "Author", " "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
