package com.example.library.service;

import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.Reader;
import com.example.library.repository.InMemoryLibraryRepository;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Implements the borrowing and return business rules for library loans.
 */
public final class LoanService {
    private static final int LOAN_PERIOD_DAYS = 14;

    private final InMemoryLibraryRepository repository;
    private final Clock clock;

    /**
     * Creates a service using the system default clock.
     *
     * @param repository repository used to store library data
     */
    public LoanService(InMemoryLibraryRepository repository) {
        this(repository, Clock.systemDefaultZone());
    }

    /**
     * Creates a service with an explicit clock.
     *
     * <p>The clock is injectable so tests can remain deterministic.</p>
     *
     * @param repository repository used to store library data
     * @param clock source of the current date
     */
    public LoanService(InMemoryLibraryRepository repository, Clock clock) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
        this.clock = Objects.requireNonNull(clock, "clock must not be null");
    }

    /**
     * Borrows an available book for a known reader.
     *
     * @param bookId book identifier
     * @param readerId reader identifier
     * @return newly created loan
     * @throws IllegalArgumentException if the book or reader does not exist
     * @throws IllegalStateException if the book is already unavailable
     */
    public Loan borrowBook(String bookId, String readerId) {
        Book book = repository.findBookById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));

        Reader reader = repository.findReaderById(readerId)
                .orElse(null);
        if (reader == null) {
            throw new IllegalArgumentException("Reader not found: " + readerId);
        }

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available: " + bookId);
        }

        LocalDate loanDate = LocalDate.now(clock);
        Loan loan = new Loan(
                UUID.randomUUID().toString(),
                book,
                reader,
                loanDate,
                loanDate.plusDays(LOAN_PERIOD_DAYS));

        book.markUnavailable();
        repository.saveLoan(loan);
        return loan;
    }

    /**
     * Returns an open loan and makes its book available again.
     *
     * @param loanId loan identifier
     * @throws IllegalArgumentException if the loan does not exist
     * @throws IllegalStateException if the loan has already been returned
     */
    public void returnBook(String loanId) {
        Loan loan = repository.findLoanById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));

        if (!loan.isOpen()) {
            throw new IllegalStateException("Loan has already been returned: " + loanId);
        }

        loan.setReturnDate(LocalDate.now(clock));
        loan.getBook().markAvailable();
    }

    /**
     * Finds loans that are overdue and still open.
     *
     * @param today reference date used to determine overdue status
     * @return overdue open loans
     * @throws NullPointerException if {@code today} is null
     */
    public List<Loan> getOverdueLoans(LocalDate today) {
        Objects.requireNonNull(today, "today must not be null");
        return repository.findAllLoans().stream()
                .filter(Loan::isOpen)
                .filter(loan -> loan.getDueDate().isBefore(today))
                .toList();
    }

    /**
     * Checks whether a book exists and is currently available.
     *
     * @param bookId book identifier
     * @return {@code true} when the book exists and is available
     */
    public boolean isBookAvailable(String bookId) {
        return repository.findBookById(bookId)
                .map(Book::isAvailable)
                .orElse(false);
    }
}
