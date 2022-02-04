/*
 * This file is generated by jOOQ.
 */
package org.jooq.example.db.h2.tables.pojos;


import javax.annotation.processing.Generated;
import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BookToBookStore implements Serializable {

    private static final long serialVersionUID = 472387379;

    private String  bookStoreName;
    private Integer bookId;
    private Integer stock;

    public BookToBookStore() {}

    public BookToBookStore(BookToBookStore value) {
        this.bookStoreName = value.bookStoreName;
        this.bookId = value.bookId;
        this.stock = value.stock;
    }

    public BookToBookStore(
        String  bookStoreName,
        Integer bookId,
        Integer stock
    ) {
        this.bookStoreName = bookStoreName;
        this.bookId = bookId;
        this.stock = stock;
    }

    public String getBookStoreName() {
        return this.bookStoreName;
    }

    public void setBookStoreName(String bookStoreName) {
        this.bookStoreName = bookStoreName;
    }

    public Integer getBookId() {
        return this.bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getStock() {
        return this.stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("BookToBookStore (");

        sb.append(bookStoreName);
        sb.append(", ").append(bookId);
        sb.append(", ").append(stock);

        sb.append(")");
        return sb.toString();
    }
}
