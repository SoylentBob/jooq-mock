package com.github.soylentbob.jooq.mock;

import org.jooq.Query;
import org.jooq.Record;
import org.jooq.Table;

import static java.util.Objects.*;


/**
 * This class can be used to prepare the results of a mocked SQL
 * statement.
 */
public class MockDSLBuilderWhen {

    private final Query query;
    private final MockDSLDataProvider dataProvider;
    private final MockDSLBuilder builder;

    /**
     * Creates a new instance.
     *
     * @param query        The query that should be prepared by this
     *                     instance.
     * @param dataProvider The {@link MockDSLDataProvider} that
     * @param builder      The builder that should be used by this instance.
     */
    public MockDSLBuilderWhen(final Query query, final MockDSLDataProvider dataProvider, final MockDSLBuilder builder) {
        this.query = requireNonNull(query, "The query may not be null");
        this.dataProvider = requireNonNull(dataProvider, "The data provider may not be null");
        this.builder = requireNonNull(builder, "The builder may not be null");
    }

    /**
     * Prepares the result to make sure that it returns the supplied records.
     *
     * @param records The records that should be returned when executing the query.
     * @return The builder.
     */
    public MockDSLBuilder thenReturn(Record... records) {
        dataProvider.addQueryResult(query, records);
        return builder;
    }

    /**
     * Prepares the result to make sure that an empty result is result for the supplied table.
     *
     * @param table The table that the result should be returned for.
     * @return The builder.
     */
    public MockDSLBuilder thenReturnNothingFor(Table table) {
        dataProvider.addEmptyQueryResult(query, table);
        return builder;
    }

    /**
     * Prepares the result to make sure that it returns the supplied count.
     *
     * @param count The count of rows touched by the executed statement.
     * @return The builder.
     */
    public MockDSLBuilder thenReturn(int count) {
        dataProvider.addCountResult(query, count);
        return builder;
    }
}
