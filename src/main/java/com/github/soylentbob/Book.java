package com.github.soylentbob;

/**
 * The common interface for objects that represent a single {@link Book}
 */
public interface Book {

  /**
   * The ID of the book.
   *
   * @return The ID of the book.
   */
  Integer id();

  /**
   * Renames the book.
   *
   * @param title The new title that the book should have.
   */
  void rename(String title);
}
