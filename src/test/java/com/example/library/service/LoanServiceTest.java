package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.Reader;
import com.example.library.repository.InMemoryLibraryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LoanServiceTest {
    private static final Instant FIXED_INSTANT = Instant.parse("2026-09-17T10:15:30Z");
    private static final Clock FIXED_CLOCK = Clock.fixed(FIXED_INSTANT, ZoneOffset.UTC);
    private static final LocalDate TODAY = LocalDate.of(2026, 9, 17);

    private InMemoryLibraryRepository repository;
    private LoanService service;
    private Book book;
    private Reader reader;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLibraryRepository();
        service = new LoanService(repository, FIXED_CLOCK);
        book = new Book("book-1", "Title", "Author", "123456789X");
        reader = new Reader("reader-1", "Alice", "alice@example.com");
        repository.addBook(book);
        repository.addReader(reader);
    }

    @Test
    void shouldBorrowAvailableBook() {
        Loan loan = service.borrowBook("book-1", "reader-1");

        assertThat(loan.getBook()).isSameAs(book);
        assertThat(loan.getReader()).isSameAs(reader);
        assertThat(loan.getLoanDate()).isEqualTo(TODAY);
        assertThat(loan.getDueDate()).isEqualTo(TODAY.plusDays(14));
        assertThat(book.isAvailable()).isFalse();
        assertThat(loan.getId()).isNotBlank();
        assertThat(repository.findAllLoans()).containsExactly(loan);
    }

    @Test
    void shouldRejectBorrowingUnavailableBook() {
        book.markUnavailable();

        assertThatThrownBy(() -> service.borrowBook("book-1", "reader-1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Book is not available: book-1");
    }

    @Test
    void shouldRejectMissingBook() {
        assertThatThrownBy(() -> service.borrowBook("missing", "reader-1"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book not found: missing");
    }

    @Test
    void shouldReturnBook() {
        Loan loan = service.borrowBook("book-1", "reader-1");

        service.returnBook(loan.getId());

        assertThat(loan.getReturnDate()).isEqualTo(TODAY);
        assertThat(book.isAvailable()).isTrue();
    }

    @Test
    void shouldRejectReturningUnknownLoan() {
        assertThatThrownBy(() -> service.returnBook("missing"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loan not found: missing");
    }

    @Test
    void shouldRejectReturningSameLoanTwice() {
        Loan loan = service.borrowBook("book-1", "reader-1");
        service.returnBook(loan.getId());

        assertThatThrownBy(() -> service.returnBook(loan.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Loan has already been returned: " + loan.getId());
    }

    @Test
    void shouldRejectBorrowingSameBookTwice() {
        service.borrowBook("book-1", "reader-1");

        assertThatThrownBy(() -> service.borrowBook("book-1", "reader-1"))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldFindOverdueLoans() {
        Loan overdue = new Loan(
                "loan-overdue",
                book,
                reader,
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 15));
        repository.saveLoan(overdue);

        List<Loan> result = service.getOverdueLoans(TODAY);

        assertThat(result).containsExactly(overdue);
    }

    @Test
    void shouldNotTreatLoanDueTodayAsOverdue() {
        Loan dueToday = new Loan(
                "loan-due-today",
                book,
                reader,
                LocalDate.of(2026, 9, 3),
                TODAY);
        repository.saveLoan(dueToday);

        assertThat(service.getOverdueLoans(TODAY)).isEmpty();
    }

    @Test
    void shouldIgnoreReturnedLoanWhenFindingOverdueLoans() {
        Loan returned = new Loan(
                "loan-returned",
                book,
                reader,
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 15));
        returned.setReturnDate(LocalDate.of(2026, 9, 10));
        repository.saveLoan(returned);

        assertThat(service.getOverdueLoans(TODAY)).isEmpty();
    }

    @Test
    void shouldRejectNullOverdueReferenceDate() {
        assertThatThrownBy(() -> service.getOverdueLoans(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("today must not be null");
    }

    @Test
    void shouldReportBookAvailability() {
        assertThat(service.isBookAvailable("book-1")).isTrue();

        book.markUnavailable();

        assertThat(service.isBookAvailable("book-1")).isFalse();
    }

    @Test
    void shouldReturnFalseForUnknownBookAvailability() {
        assertThat(service.isBookAvailable("missing")).isFalse();
    }

    @Test
    void shouldCreateServiceWithDefaultClock() {
        LoanService defaultService = new LoanService(repository);

        assertThat(defaultService.isBookAvailable("book-1")).isTrue();
    }

    @Test
    void shouldRejectNullRepositoryOrClock() {
        assertThatThrownBy(() -> new LoanService(null, FIXED_CLOCK))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("repository must not be null");
        assertThatThrownBy(() -> new LoanService(repository, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("clock must not be null");
    }
}
