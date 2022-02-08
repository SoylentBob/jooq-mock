package com.github.soylentbob.sql;

import com.github.soylentbob.Books;
import com.github.soylentbob.jooq.mock.MockDSLBuilder;
import org.assertj.core.api.SoftAssertions;
import org.jooq.DSLContext;
import org.jooq.Record2;
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
   * Tests whether the {@link SqlBooks} class handles retrieving an
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
   * Tests whether the {@link SqlBooks} class handles retrieving a
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

  /**
   * A query with selected fields should be handled correctly.
   */
  @Test
  public void selectedFieldsQuery() {
    final BookRecord preparedResult = new BookRecord();

    preparedResult.setId(1);
    preparedResult.setTitle("Die Leiden des jungen Werthers");

    DSLContext sql = new MockDSLBuilder(SQLDialect.H2)
            .whenSelectedFieldsQuery(DSL.using(SQLDialect.H2)
                    .select(Tables.BOOK.TITLE, Tables.BOOK.ID)
                    .from(Tables.BOOK)
                    .where(Tables.BOOK.ID.eq(1)))
            .thenReturn(preparedResult)
            .context();

    final Record2<String, Integer> result =
            sql.select(Tables.BOOK.TITLE, Tables.BOOK.ID)
                    .from(Tables.BOOK)
                    .where(Tables.BOOK.ID.eq(1))
                    .fetchOne();

    final SoftAssertions assertions = new SoftAssertions();

    assertions.assertThat(result.get(Tables.BOOK.TITLE))
            .isEqualTo("Die Leiden des jungen Werthers");
    assertions.assertThat(result.get(Tables.BOOK.ID))
            .isEqualTo(1);

    assertions.assertAll();
  }

  /**
   * Multiple mocked results should be handled correctly.
   */
  @Test
  public void multipleMockedResults() {
    final BookRecord firstRecord = new BookRecord();
    firstRecord.setId(1);

    final BookRecord secondRecord = new BookRecord();
    secondRecord.setId(2);

    DSLContext sql = new MockDSLBuilder(SQLDialect.H2)
            .when(DSL.using(SQLDialect.H2)
                    .selectFrom(Tables.BOOK)
                    .where(Tables.BOOK.ID.eq(1)))
            .thenReturn(firstRecord)
            .when(DSL.using(SQLDialect.H2)
                    .selectFrom(Tables.BOOK)
                    .where(Tables.BOOK.ID.eq(2)))
            .thenReturn(secondRecord)
            .context();

    final Books books = new SqlBooks(sql);

    final SoftAssertions assertions = new SoftAssertions();

    assertions.assertThat(books.fetchBookById(1))
            .hasValueSatisfying(book -> assertThat(book.id()).isEqualTo(1));
    assertions.assertThat(books.fetchBookById(2))
            .hasValueSatisfying(book -> assertThat(book.id()).isEqualTo(2));

    assertions.assertAll();
  }
}
