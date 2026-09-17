package com.example.library.repository;

import java.util.List;
import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InMemoryLibraryRepositoryTest {
    private InMemoryLibraryRepository repository;
    private Book book;
    private Reader reader;
    private Loan loan;

    @BeforeEach
    void setUp() {
        repository = new InMemoryLibraryRepository();
        book = new Book("book-1", "Title", "Author", "123456789X");
        reader = new Reader("reader-1", "Alice", "alice@example.com");
        loan = new Loan(
                "loan-1",
                book,
                reader,
                LocalDate.of(2026, 9, 17),
                LocalDate.of(2026, 10, 1));
    }

    @Test
    void shouldAddAndFindBook() {
        repository.addBook(book);

        assertThat(repository.findBookById("book-1")).containsSame(book);
    }

    @Test
    void shouldReturnEmptyForMissingBook() {
        assertThat(repository.findBookById("missing")).isEmpty();
    }

    @Test
    void shouldReturnAllBooksInInsertionOrder() {
        Book second = new Book("book-2", "Title 2", "Author 2", "987654321X");
        repository.addBook(book);
        repository.addBook(second);

        assertThat(repository.findAllBooks()).containsExactly(book, second);
    }

    @Test
    void shouldRejectDuplicateBookId() {
        repository.addBook(book);

        assertThatThrownBy(() -> repository.addBook(
                new Book("book-1", "Another", "Author", "9780306406157")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Book already exists: book-1");
    }

    @Test
    void shouldAddAndFindReader() {
        repository.addReader(reader);

        assertThat(repository.findReaderById("reader-1")).containsSame(reader);
        assertThat(repository.findAllReaders()).containsExactly(reader);
    }

    @Test
    void shouldReturnEmptyForMissingReader() {
        assertThat(repository.findReaderById("missing")).isEmpty();
    }

    @Test
    void shouldRejectDuplicateReaderId() {
        repository.addReader(reader);

        assertThatThrownBy(() -> repository.addReader(
                new Reader("reader-1", "Bob", "bob@example.com")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Reader already exists: reader-1");
    }

    @Test
    void shouldSaveAndFindLoan() {
        repository.saveLoan(loan);

        assertThat(repository.findLoanById("loan-1")).containsSame(loan);
        assertThat(repository.findAllLoans()).containsExactly(loan);
    }

    @Test
    void shouldRejectDuplicateLoanId() {
        repository.saveLoan(loan);

        assertThatThrownBy(() -> repository.saveLoan(
                new Loan(
                        "loan-1",
                        book,
                        reader,
                        LocalDate.of(2026, 9, 18),
                        LocalDate.of(2026, 10, 2))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Loan already exists: loan-1");
    }

    @Test
    void shouldReturnIndependentBookList() {
        repository.addBook(book);

        List<Book> books = repository.findAllBooks();
        books.clear();

        assertThat(repository.findAllBooks()).containsExactly(book);
    }

    @Test
    void shouldReturnIndependentReaderList() {
        repository.addReader(reader);

        List<Reader> readers = repository.findAllReaders();
        readers.clear();

        assertThat(repository.findAllReaders()).containsExactly(reader);
    }

    @Test
    void shouldReturnIndependentLoanList() {
        repository.saveLoan(loan);

        List<Loan> loans = repository.findAllLoans();
        loans.clear();

        assertThat(repository.findAllLoans()).containsExactly(loan);
    }

    @Test
    void shouldRejectNullEntities() {
        assertThatThrownBy(() -> repository.addBook(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> repository.addReader(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> repository.saveLoan(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
