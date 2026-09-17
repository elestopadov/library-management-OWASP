package com.example.library.integration;

import com.example.library.model.Book;
import com.example.library.model.Reader;
import com.example.library.repository.InMemoryLibraryRepository;
import com.example.library.service.LoanService;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class LibraryFlowIT {
    @Test
    void shouldBorrowAndReturnBookThroughApplicationFlow() {
        InMemoryLibraryRepository repository = new InMemoryLibraryRepository();
        Book book = new Book("book-1", "Clean Code", "Robert C. Martin", "9780132350884");
        Reader reader = new Reader("reader-1", "Alice", "alice@example.com");
        repository.addBook(book);
        repository.addReader(reader);

        Clock clock = Clock.fixed(
                LocalDate.of(2026, 9, 17).atStartOfDay(ZoneOffset.UTC).toInstant(),
                ZoneOffset.UTC);
        LoanService service = new LoanService(repository, clock);
        var loan = service.borrowBook(book.getId(), reader.getId());

        assertThat(service.isBookAvailable(book.getId())).isFalse();
        assertThat(repository.findLoanById(loan.getId())).containsSame(loan);
        assertThat(loan.getLoanDate()).isEqualTo(LocalDate.of(2026, 9, 17));

        service.returnBook(loan.getId());

        assertThat(service.isBookAvailable(book.getId())).isTrue();
        assertThat(loan.getReturnDate()).isEqualTo(LocalDate.of(2026, 9, 17));
    }
}
