package com.github.soylentbob.jooq.mock;

import org.jooq.DSLContext;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;

import static java.util.Objects.requireNonNull;

/**
 * This class is used to provide access and ease building a mocked {@link org.jooq.DSLContext} that
 * you can use in your unit tests.
 */
public class MockDSLBuilder {

    private final SQLDialect dialect;
    private final MockDSLDataProvider dataProvider;

    /**
     * Creates a new instance.
     */
    public MockDSLBuilder() {
        this(SQLDialect.DEFAULT);
    }

    /**
     * Creates a new instance.
     *
     * @param dialect The {@link SQLDialect} that should be used by the resulting {@link DSLContext}.
     *                This parameter may not be null.
     */
    public MockDSLBuilder(final SQLDialect dialect) {
        this.dialect = requireNonNull(dialect, "The dialect may not be null");
        this.dataProvider = new MockDSLDataProvider(dialect);
    }

    public MockDSLBuilderWhen when(final Query query) {
        return new MockDSLBuilderWhen(query, dataProvider, this);
    }

    /**
     * Builds the prepared {@link DSLContext}.
     *
     * @return The {@link DSLContext}
     */
    public DSLContext context() {
        return DSL.using(new MockConnection(dataProvider), dialect);
    }

}
