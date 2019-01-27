package com.github.soylentbob.sql;

import com.github.soylentbob.jooq.mock.MockDSLBuilder;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.example.db.h2.Tables;
import org.jooq.example.db.h2.tables.records.BookRecord;
import org.jooq.impl.DSL;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * A set of tests that are used to evaluate the current implementation of the
 * {@link SqlBooks} class.
 */
public class SqlBooksTest {

    /**
     * Tests whether or not the {@link SqlBooks} class handles retrieving an
     * existing book correctly.
     */
    @Test
    public void existingBook() {

        DSLContext sql = new MockDSLBuilder(SQLDialect.H2)
                .when(DSL.using(SQLDialect.H2)
                        .selectFrom(Tables.BOOK)
                        .where(Tables.BOOK.ID.eq(1)))
                .thenReturn(new BookRecord())
                .context();

        assertThat(new SqlBooks(sql)
                .fetchBookById(1)).isPresent();
    }

    /**
     * Tests whether or not the {@link SqlBooks} class handles retrieving a
     * non-existent book correctly.
     */
    @Test
    public void nonExistentBook() {

        DSLContext sql = new MockDSLBuilder(SQLDialect.H2)
                .when(DSL.using(SQLDialect.H2)
                        .selectFrom(Tables.BOOK)
                        .where(Tables.BOOK.ID.eq(1)))
                .thenReturnNothingFor(Tables.BOOK)
                .context();

        assertThat(new SqlBooks(sql).fetchBookById(1)).isNotPresent();
    }
}
