package com.github.soylentbob.jooq.mock;

import org.jooq.Field;
import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.impl.DSL;
import org.jooq.impl.TableRecordImpl;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * An implementation of the {@link MockDataProvider} interface that handles
 * providing the data to your unit tests based on the setup described through
 * the {@link MockDSLBuilder}.
 */
public class MockDSLDataProvider implements MockDataProvider {

  private final SQLDialect dialect;
  private Map<String, MockResult[]> results = new HashMap<>();

  /**
   * Creates a new instance.
   *
   * @param dialect The {@link SQLDialect} that should be used by this instance.
   *                This parameter may not be null.
   */
  public MockDSLDataProvider(SQLDialect dialect) {
    this.dialect = requireNonNull(dialect, "The dialect may not be null");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MockResult[] execute(MockExecuteContext ctx) throws SQLException {

    String statement = ctx.sql();

    if (results.containsKey(statement)) {
      return results.get(statement);
    } else {
      throw new SQLException("Unexpected SQL statement. Check your test setup.");
    }
  }

  /**
   * Adds a query result to the data provider for the given query.
   *
   * @param query   The query that the results should be stored for.
   * @param records The records that should be returned by the query.
   */
  public void addQueryResult(Query query, Record[] records) {

    MockResult[] results = new MockResult[records.length];

    for (int i = 0; i < records.length; i++) {
      results[i] = new MockResult(records[i]);
    }

    this.results.put(query.getSQL(), results);
  }

  public <Q extends Query & TableLike<R>, R extends Record> void addTableLikeQueryResult(Q query, Record[] records) {

    MockResult[] results = new MockResult[records.length];

    for (int i = 0; i < records.length; i++) {
      final Record actualRecord = records[i];

      results[i] = new MockResult(actualRecord.into(query.fields()));
    }

    this.results.put(query.getSQL(), results);
  }

  private <T> void mapRecordParameter(final Record actualRecord,
                                      final Record mappedRecord,
                                      final Field<T> field) {
    mappedRecord.set(field, actualRecord.get(field));
  }

  /**
   * Adds an empty query result to the data provider for the given query.
   *
   * @param query The query that the result should be stored for.
   * @param table The table that the result should be stored for.
   */
  public void addEmptyQueryResult(Query query, Table<?> table) {

    Result record = DSL.using(dialect).newResult(table);

    MockResult[] result = new MockResult[]{new MockResult(0, record)};

    this.results.put(query.getSQL(), result);
  }

  /**
   * Adds a count result to the data provider for the given query.
   *
   * @param query The query that the result should be stored for.
   * @param i     The count of rows affected by the query.
   */
  public void addCountResult(Query query, int i) {
    this.results.put(query.getSQL(), new MockResult[]{new MockResult(i)});
  }
}
