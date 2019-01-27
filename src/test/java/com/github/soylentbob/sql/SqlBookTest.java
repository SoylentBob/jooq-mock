package com.github.soylentbob.sql;

import com.github.soylentbob.jooq.mock.MockDSLBuilder;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.example.db.h2.Tables;
import org.jooq.example.db.h2.tables.records.BookRecord;
import org.jooq.impl.DSL;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

/**
 * A set of tests that can be used to evaluate the {@link SqlBook} class.
 */
public class SqlBookTest {

    /**
     * Tests the successful renaming of a book.
     */
    @Test
    public void successfulRenaming() {

        DSLContext sql = new MockDSLBuilder(SQLDialect.H2)
                .when(DSL.using(SQLDialect.H2)
                        .update(Tables.BOOK)
                        .set(Tables.BOOK.TITLE, "New Title")
                        .where(Tables.BOOK.ID.eq(1)))
                .thenReturn(1)
                .context();

        BookRecord record = new BookRecord(1, null, null, null, null, null, null, null, null, null, null);

        new SqlBook(sql, record).rename("New Title");
    }

    /**
     * Tests a failed renaming of a book.
     */
    @Test
    public void failedRenaming() {

        DSLContext sql = new MockDSLBuilder(SQLDialect.H2)
                .when(DSL.using(SQLDialect.H2)
                        .update(Tables.BOOK)
                        .set(Tables.BOOK.TITLE, "New Title")
                        .where(Tables.BOOK.ID.eq(1)))
                .thenReturn(0)
                .context();

        BookRecord record = new BookRecord(1, null, null, null, null, null, null, null, null, null, null);

        try {
            new SqlBook(sql, record).rename("New Title");
            failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("The book could not be renamed");
        }
    }

}
