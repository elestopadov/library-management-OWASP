package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.Reader;
import com.example.library.repository.InMemoryLibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class LoanServiceParameterizedTest {
    private InMemoryLibraryRepository repository;
    private LoanService service;
    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLibraryRepository();
        service = new LoanService(
                repository,
                Clock.fixed(LocalDate.of(2026, 9, 17).atStartOfDay(ZoneOffset.UTC).toInstant(), ZoneOffset.UTC));
        book = new Book("book-1", "Title", "Author", "123456789X");
        reader = new Reader("reader-1", "Alice", "alice@example.com");
        repository.addBook(book);
        repository.addReader(reader);
    }

    @ParameterizedTest(name = "loanDate={0}, today={1}, expectedOverdue={2}")
    @CsvSource({
            "2026-09-17, 2026-09-17, false",
            "2026-09-17, 2026-10-01, false",
            "2026-09-01, 2026-09-16, true",
            "2026-08-01, 2026-09-17, true"
    })
    void shouldDetectOverdueLoanAccordingToDate(
            LocalDate loanDate,
            LocalDate today,
            boolean expectedOverdue) {
        Loan loan = new Loan(
                "loan-1",
                book,
                reader,
                loanDate,
                loanDate.plusDays(14));
        repository.saveLoan(loan);

        assertThat(service.getOverdueLoans(today).contains(loan))
                .isEqualTo(expectedOverdue);
    }
}
