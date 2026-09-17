package com.example.library.model;

import java.util.Objects;

/**
 * Represents a library book and its current availability.
 */
public final class Book {
    private final String id;
    private final String title;
    private final String author;
    private final String isbn;
    private boolean available;

    /**
     * Creates an available book.
     *
     * @param id unique book identifier
     * @param title book title
     * @param author book author
     * @param isbn ISBN value stored for the book
     */
    public Book(String id, String title, String author, String isbn) {
        this(id, title, author, isbn, true);
    }

    /**
     * Creates a book with the supplied availability state.
     *
     * @param id unique book identifier
     * @param title book title
     * @param author book author
     * @param isbn ISBN value stored for the book
     * @param available whether the book is currently available
     */
    public Book(String id, String title, String author, String isbn, boolean available) {
        this.id = requireText(id, "id");
        this.title = requireText(title, "title");
        this.author = requireText(author, "author");
        this.isbn = requireText(isbn, "isbn");
        this.available = available;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    /**
     * Returns the unique book identifier.
     *
     * @return book identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the book title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the book author.
     *
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Returns the stored ISBN value.
     *
     * @return ISBN
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Returns whether the book can currently be borrowed.
     *
     * @return {@code true} when the book is available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Marks the book as borrowed.
     *
     * <p>The operation is intentionally idempotent so the simple training model
     * does not expose an additional failure mode when state is synchronized by callers.</p>
     */
    public void markUnavailable() {
        this.available = false;
    }

    /**
     * Marks the book as available in the library.
     */
    public void markAvailable() {
        this.available = true;
    }

    /**
     * Compares books by their unique identifiers.
     *
     * @param other object to compare
     * @return {@code true} when both objects represent the same book id
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Book book)) {
            return false;
        }
        return Objects.equals(id, book.id);
    }

    /**
     * Returns the hash code derived from the unique book identifier.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a readable representation of the book.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", available=" + available +
                '}';
    }
}
