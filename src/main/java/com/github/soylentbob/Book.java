package com.github.soylentbob;

/**
 * The common interface for objects that represent a single {@link Book}
 */
public interface Book {

    /**
     * Renames the book.
     *
     * @param title The new title that the book should have.
     */
    public void rename(String title);
}
