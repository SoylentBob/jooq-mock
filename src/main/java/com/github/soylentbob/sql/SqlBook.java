package com.github.soylentbob.sql;

import com.github.soylentbob.Book;
import org.jooq.DSLContext;
import org.jooq.example.db.h2.Tables;
import org.jooq.example.db.h2.tables.records.BookRecord;

import static java.util.Objects.*;


/**
 * An implementation of the {@link Book} interface that makes use
 * of an underlying SQL database.
 */
public class SqlBook implements Book {

    private final DSLContext sql;
    private final BookRecord record;

    /**
     * Creates a new instance.
     *
     * @param record The record that should be represented by this
     *               instance.
     */
    public SqlBook(final DSLContext sql, final BookRecord record) {
        this.sql = requireNonNull(sql, "The SQL DSL may not be null");
        this.record = requireNonNull(record, "The record may not be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rename(final String title) {
        int updatedRows = sql.update(Tables.BOOK).set(Tables.BOOK.TITLE, title).where(Tables.BOOK.ID.eq(record.getId())).execute();

        if (updatedRows == 0) {
            throw new IllegalArgumentException("The book could not be renamed");
        }
    }
}
