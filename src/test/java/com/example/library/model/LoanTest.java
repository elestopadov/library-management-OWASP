package com.example.library.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoanTest {
    private static final LocalDate LOAN_DATE = LocalDate.of(2026, 9, 17);
    private static final LocalDate DUE_DATE = LocalDate.of(2026, 10, 1);

    @Test
    void shouldCreateLoanWithoutReturnDate() {
        Book book = new Book("book-1", "Title", "Author", "123456789X");
        Reader reader = new Reader("reader-1", "Alice", "alice@example.com");

        Loan loan = new Loan("loan-1", book, reader, LOAN_DATE, DUE_DATE);

        assertThat(loan.getId()).isEqualTo("loan-1");
        assertThat(loan.getBook()).isSameAs(book);
        assertThat(loan.getReader()).isSameAs(reader);
        assertThat(loan.getLoanDate()).isEqualTo(LOAN_DATE);
        assertThat(loan.getDueDate()).isEqualTo(DUE_DATE);
        assertThat(loan.getReturnDate()).isNull();
    }

    @Test
    void shouldReportOpenStateBeforeAndAfterReturn() {
        Loan loan = createLoan();

        assertThat(loan.isOpen()).isTrue();

        loan.setReturnDate(LocalDate.of(2026, 9, 20));

        assertThat(loan.isOpen()).isFalse();
    }

    @Test
    void shouldSetReturnDateOnce() {
        Loan loan = createLoan();

        loan.setReturnDate(LocalDate.of(2026, 9, 20));

        assertThat(loan.getReturnDate()).isEqualTo(LocalDate.of(2026, 9, 20));
        assertThatThrownBy(() -> loan.setReturnDate(LocalDate.of(2026, 9, 21)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldRejectInvalidConstructorArguments() {
        Book book = new Book("book-1", "Title", "Author", "123456789X");
        Reader reader = new Reader("reader-1", "Alice", "alice@example.com");

        assertThatThrownBy(() -> new Loan(null, book, reader, LOAN_DATE, DUE_DATE))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Loan(" ", book, reader, LOAN_DATE, DUE_DATE))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Loan("loan-1", null, reader, LOAN_DATE, DUE_DATE))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Loan("loan-1", book, null, LOAN_DATE, DUE_DATE))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Loan("loan-1", book, reader, null, DUE_DATE))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Loan("loan-1", book, reader, LOAN_DATE, null))
                .isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new Loan("loan-1", book, reader, LOAN_DATE, LOAN_DATE.minusDays(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectInvalidReturnDate() {
        Loan loan = createLoan();

        assertThatThrownBy(() -> loan.setReturnDate(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> loan.setReturnDate(LOAN_DATE.minusDays(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldUseIdForEqualityAndHashCode() {
        Book book = new Book("book-1", "Title", "Author", "123456789X");
        Reader reader = new Reader("reader-1", "Alice", "alice@example.com");
        Loan first = new Loan("loan-1", book, reader, LOAN_DATE, DUE_DATE);
        Loan second = new Loan(
                "loan-1", book, reader, LOAN_DATE.plusDays(1), DUE_DATE.plusDays(1));

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("loan-1");
    }

    @Test
    void shouldContainUsefulValuesInToString() {
        Loan loan = createLoan();

        assertThat(loan.toString())
                .contains("loan-1", "book-1", "reader-1", "2026-09-17", "2026-10-01");
    }

    private Loan createLoan() {
        Book book = new Book("book-1", "Title", "Author", "123456789X");
        Reader reader = new Reader("reader-1", "Alice", "alice@example.com");
        return new Loan("loan-1", book, reader, LOAN_DATE, DUE_DATE);
    }
}
