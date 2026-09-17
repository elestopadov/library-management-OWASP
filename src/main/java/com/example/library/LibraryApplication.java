package com.example.library;

import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.Reader;
import com.example.library.repository.InMemoryLibraryRepository;
import com.example.library.service.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Small command-line entry point used to demonstrate the application flow and JSON output.
 */
public final class LibraryApplication {
    private LibraryApplication() {
    }

    /**
     * Creates a sample loan and prints it as formatted JSON.
     *
     * @param args command-line arguments; not used
     * @throws JsonProcessingException if Jackson cannot serialize the loan
     */
    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(createSampleLoanJson());
    }

    /**
     * Creates the demonstration flow used by the command-line application.
     *
     * @return formatted JSON representation of the sample loan
     * @throws JsonProcessingException if Jackson cannot serialize the loan
     */
    static String createSampleLoanJson() throws JsonProcessingException {
        InMemoryLibraryRepository repository = new InMemoryLibraryRepository();

        Book book = new Book(
                "book-1",
                "Clean Code",
                "Robert C. Martin",
                "978-0132350884");
        Reader reader = new Reader(
                "reader-1",
                "Alice Example",
                "alice@example.com");

        repository.addBook(book);
        repository.addReader(reader);

        LoanService loanService = new LoanService(repository);
        Loan loan = loanService.borrowBook(book.getId(), reader.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(loan);
    }
}
