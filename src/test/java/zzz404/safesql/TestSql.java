package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TestSql {
    @AfterEach
    void afterEach() {
        QueryContext.clear();
    }

    @Test
    void test_sql_useDefault() {
        sql("hi");
        QueryContext ctx = QueryContext.get();
        assertEquals("", ctx.getName());
    }

    @Test
    void test_from_useDefault() {
        from(Object.class);
        QueryContext ctx = QueryContext.get();
        assertEquals("", ctx.getName());
    }

    @Test
    void test_use() {
        use("ccc");
        QueryContext ctx = QueryContext.get();
        assertEquals("ccc", ctx.getName());
    }

    @Test
    void test_useDefault() {
        use();
        QueryContext ctx = QueryContext.get();
        assertEquals("", ctx.getName());
    }
}
