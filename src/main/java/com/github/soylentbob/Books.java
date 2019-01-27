package com.github.soylentbob;

import java.util.Optional;

/**
 * The common interface for objects that represent a collection of books.
 */
public interface Books {

    /**
     * Fetches a specific book by its ID.
     *
     * @param bookId The ID of the book that should be retrieved.
     * @return The {@link Book} or {@link Optional#empty()} if
     * no book with the specified ID exists.
     */
    public Optional<Book> fetchBookById(Integer bookId);
}
