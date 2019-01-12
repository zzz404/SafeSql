package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.QueryContext;

public class TestQueryContext {

    @Test
    void test_get_noContext_throwException() {
        assertThrows(NullPointerException.class, () -> QueryContext.get());
    }

    @Test
    void test_get_underQueryContext() {
        QueryContext.underQueryContext(ctx -> {
            assertEquals(ctx, QueryContext.get());
        });
    }

}
