package com.github.soylentbob.sql;

import com.github.soylentbob.Book;
import com.github.soylentbob.Books;
import org.jooq.DSLContext;
import org.jooq.example.db.h2.Tables;
import org.jooq.example.db.h2.tables.records.BookRecord;

import java.util.Optional;

import static java.util.Objects.*;

/**
 * An implementation of the {@link Books} interface that makes use of an undlerying SQL database.
 */
public class SqlBooks implements Books {

    private final DSLContext sql;

    /**
     * Creates a new instance.
     *
     * @param sql The SQL DSL that should be used by this instance.
     */
    public SqlBooks(final DSLContext sql) {

        this.sql = requireNonNull(sql, "The SQL DSL may not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Book> fetchBookById(final Integer bookId) {

        BookRecord record = sql.selectFrom(Tables.BOOK).where(Tables.BOOK.ID.eq(bookId)).fetchOne();

        return Optional.ofNullable(record).map(v -> new SqlBook(sql, record));
    }
}
