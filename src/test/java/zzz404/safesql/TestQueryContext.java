package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

    @Test
    void test_get_notUnderQueryContext_throwNull() {
        QueryContext.underQueryContext(ctx -> {
        });
        assertThrows(NullPointerException.class, () -> QueryContext.get());
    }

    @Test
    void test_others() {
        QueryContext.underQueryContext(ctx -> {
            assertTrue(ctx.getConditions().isEmpty());
            assertTrue(ctx.getOrderBys().isEmpty());
        });
    }

}
