package com.example.library.repository;

import com.example.library.model.Book;
import com.example.library.model.Loan;
import com.example.library.model.Reader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Stores library data in memory for the training application.
 */
public final class InMemoryLibraryRepository {
    private final Map<String, Book> books = new LinkedHashMap<>();
    private final Map<String, Reader> readers = new LinkedHashMap<>();
    private final Map<String, Loan> loans = new LinkedHashMap<>();

    /**
     * Creates an empty in-memory repository.
     */
    public InMemoryLibraryRepository() {
    }

    /**
     * Adds a book.
     *
     * @param book book to store
     * @throws IllegalArgumentException if the book is null or its id already exists
     */
    public void addBook(Book book) {
        requireEntity(book, "book");
        rejectDuplicate(books, book.getId(), "Book");
        books.put(book.getId(), book);
    }

    /**
     * Finds a book by id.
     *
     * @param id book identifier
     * @return matching book, or empty when it does not exist
     */
    public Optional<Book> findBookById(String id) {
        return Optional.ofNullable(books.get(id));
    }

    /**
     * Returns all books in insertion order.
     *
     * @return copy of the stored books
     */
    public List<Book> findAllBooks() {
        return new ArrayList<>(books.values());
    }

    /**
     * Adds a reader.
     *
     * @param reader reader to store
     * @throws IllegalArgumentException if the reader is null or its id already exists
     */
    public void addReader(Reader reader) {
        requireEntity(reader, "reader");
        rejectDuplicate(readers, reader.getId(), "Reader");
        readers.put(reader.getId(), reader);
    }

    /**
     * Finds a reader by id.
     *
     * @param id reader identifier
     * @return matching reader, or empty when it does not exist
     */
    public Optional<Reader> findReaderById(String id) {
        return Optional.ofNullable(readers.get(id));
    }

    /**
     * Returns all readers in insertion order.
     *
     * @return copy of the stored readers
     */
    public List<Reader> findAllReaders() {
        return new ArrayList<>(readers.values());
    }

    /**
     * Stores a new loan.
     *
     * @param loan loan to store
     * @throws IllegalArgumentException if the loan is null or its id already exists
     */
    public void saveLoan(Loan loan) {
        requireEntity(loan, "loan");
        rejectDuplicate(loans, loan.getId(), "Loan");
        loans.put(loan.getId(), loan);
    }

    /**
     * Finds a loan by id.
     *
     * @param id loan identifier
     * @return matching loan, or empty when it does not exist
     */
    public Optional<Loan> findLoanById(String id) {
        return Optional.ofNullable(loans.get(id));
    }

    /**
     * Returns all stored loans in insertion order.
     *
     * @return copy of the stored loans
     */
    public List<Loan> findAllLoans() {
        return new ArrayList<>(loans.values());
    }

    private static void requireEntity(Object entity, String entityName) {
        if (entity == null) {
            throw new IllegalArgumentException(entityName + " must not be null");
        }
    }

    private static void rejectDuplicate(Map<String, ?> values, String id, String entityName) {
        if (values.containsKey(id)) {
            throw new IllegalArgumentException(entityName + " already exists: " + id);
        }
    }
}
