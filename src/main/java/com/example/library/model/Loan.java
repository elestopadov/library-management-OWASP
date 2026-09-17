package com.example.library.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a book loan between a book and a reader.
 */
public final class Loan {
    private final String id;
    private final Book book;
    private final Reader reader;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private LocalDate returnDate;

    /**
     * Creates an open loan.
     *
     * @param id unique loan identifier
     * @param book borrowed book
     * @param reader borrowing reader
     * @param loanDate date on which the book was borrowed
     * @param dueDate date on which the book is due
     */
    public Loan(
            String id,
            Book book,
            Reader reader,
            LocalDate loanDate,
            LocalDate dueDate) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        this.id = id;
        this.book = Objects.requireNonNull(book, "book must not be null");
        this.reader = Objects.requireNonNull(reader, "reader must not be null");
        this.loanDate = Objects.requireNonNull(loanDate, "loanDate must not be null");
        this.dueDate = Objects.requireNonNull(dueDate, "dueDate must not be null");
        if (dueDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("dueDate must not be before loanDate");
        }
    }

    /**
     * Returns the unique loan identifier.
     *
     * @return loan identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the borrowed book.
     *
     * @return book
     */
    public Book getBook() {
        return book;
    }

    /**
     * Returns the reader who borrowed the book.
     *
     * @return reader
     */
    public Reader getReader() {
        return reader;
    }

    /**
     * Returns the loan date.
     *
     * @return loan date
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * Returns the due date.
     *
     * @return due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Returns the return date, or {@code null} while the loan is open.
     *
     * @return return date or {@code null}
     */
    public LocalDate getReturnDate() {
        return returnDate;
    }

    /**
     * Closes this loan by assigning its return date.
     *
     * @param returnDate date on which the book was returned
     * @throws IllegalArgumentException if the return date is null or before the loan date
     * @throws IllegalStateException if the loan has already been returned
     */
    public void setReturnDate(LocalDate returnDate) {
        if (returnDate == null) {
            throw new IllegalArgumentException("returnDate must not be null");
        }
        if (this.returnDate != null) {
            throw new IllegalStateException("Loan has already been returned: " + id);
        }
        if (returnDate.isBefore(loanDate)) {
            throw new IllegalArgumentException("returnDate must not be before loanDate");
        }
        this.returnDate = returnDate;
    }

    /**
     * Returns whether this loan is still open.
     *
     * @return {@code true} when the book has not been returned yet
     */
    public boolean isOpen() {
        return returnDate == null;
    }

    /**
     * Compares loans by their unique identifiers.
     *
     * @param other object to compare
     * @return {@code true} when both objects represent the same loan id
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Loan loan)) {
            return false;
        }
        return Objects.equals(id, loan.id);
    }

    /**
     * Returns the hash code derived from the unique loan identifier.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Returns a readable representation of the loan.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", book=" + book.getId() +
                ", reader=" + reader.getId() +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                '}';
    }
}
