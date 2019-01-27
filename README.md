# jOOQ Mock


I love building applications that make use of jOOQ to integrate a database, but I hate testing them, because testing the application using the built-in tools is verbose and error prone, and I always feel like I'm taking a lot of time writing them.

The intention behind this project is to make testing code that integrates with jOOQ as easy as mocking an interface in your tests using something like Mockito.

Currently this is just an experimental project that I maintain in my spare time.

Right now the proposals can be found in the `src/test/java` directory in the `com.github.soylentbob.jooq.mock` package.

The entrypoint for creating mocked jOOQ results is the `MockDSLBuilder`.

By using this class you can simplify building a `MockDataProvider` and the `DSLContext` that can be used by your tests.

## Examples

The examples provided in this project are based on the bookstore data model taken directly from jOOQs examples.

