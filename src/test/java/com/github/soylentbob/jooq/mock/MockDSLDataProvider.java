package com.github.soylentbob.jooq.mock;

import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableLike;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

import java.sql.SQLException;
import java.util.Collection;
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
  private final Map<String, MockResult[]> results = new HashMap<>();

  /**
   * Creates a new instance.
   *
   * @param dialect The {@link SQLDialect} that should be used by this instance.
   *                This parameter may not be null.
   */
  public MockDSLDataProvider(final SQLDialect dialect) {
    this.dialect = requireNonNull(dialect, "The dialect may not be null");
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MockResult[] execute(final MockExecuteContext ctx) throws SQLException {
    final String statement =
            buildStatement(ctx.sql(), ctx.bindings());

    if (results.containsKey(statement)) {
      return results.get(statement);
    } else {
      throw new SQLException("Unexpected SQL statement. Check your test setup.");
    }
  }

  /**
   * Builds a statement for the given template and bindings.
   *
   * @param template The SQL template.
   * @param bindings The bindings.
   * @return The statement.
   */
  private String buildStatement(final String template,
                                final Object[] bindings) {
    return DSL.using(this.dialect).query(template, bindings).toString();
  }

  /**
   * Builds a statement for the given template and bindings.
   *
   * @param template The SQL template.
   * @param bindings The bindings.
   * @return The statement.
   */
  private String buildStatement(final String template,
                                final Collection<Object> bindings) {
    return buildStatement(template, bindings.toArray());
  }

  /**
   * Adds a query result to the data provider for the given query.
   *
   * @param query   The query that the results should be stored for.
   * @param records The records that should be returned by the query.
   */
  public void addQueryResult(final Query query,
                             final Record[] records) {

    final MockResult[] results = new MockResult[records.length];

    for (int i = 0; i < records.length; i++) {
      results[i] = new MockResult(records[i]);
    }

    this.results.put(buildStatement(query.getSQL(), query.getBindValues()), results);
  }

  /**
   * Adds a table like query result.
   *
   * @param query   The query that the result should be returned for.
   * @param records The records.
   * @param <Q>     The type of the query.
   * @param <R>     The type of the table.
   */
  public <Q extends Query & TableLike<R>, R extends Record> void addTableLikeQueryResult(
          final Q query,
          final Record[] records) {

    final MockResult[] results = new MockResult[records.length];

    for (int i = 0; i < records.length; i++) {
      final Record actualRecord = records[i];

      results[i] = new MockResult(actualRecord.into(query.fields()));
    }

    this.results.put(buildStatement(query.getSQL(), query.getBindValues()), results);
  }

  /**
   * Adds an empty query result to the data provider for the given query.
   *
   * @param query The query that the result should be stored for.
   * @param table The table that the result should be stored for.
   */
  public void addEmptyQueryResult(Query query, Table<?> table) {

    final Result record = DSL.using(dialect).newResult(table);

    MockResult[] result = new MockResult[]{new MockResult(0, record)};

    this.results.put(buildStatement(query.getSQL(), query.getBindValues()), result);
  }

  /**
   * Adds a count result to the data provider for the given query.
   *
   * @param query The query that the result should be stored for.
   * @param i     The count of rows affected by the query.
   */
  public void addCountResult(Query query, int i) {
    this.results.put(buildStatement(query.getSQL(), query.getBindValues()),
            new MockResult[]{new MockResult(i)});
  }
}
