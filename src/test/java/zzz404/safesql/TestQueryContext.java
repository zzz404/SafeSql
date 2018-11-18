package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class TestQueryContext {
    @AfterEach
    void afterEach() {
        QueryContext.clear();
    }

    @Test
    void test_get_noContext_throwException() {
        assertThrows(NoQueryContextException.class, () -> QueryContext.get());
    }

    void test_get_create() {
        QueryContext.create("zzz");
        QueryContext ctx = QueryContext.get();
        assertEquals("zzz", ctx.getName());
    }

    void test_get_clear() {
        QueryContext.create("zzz");
        QueryContext.clear();
        assertThrows(NoQueryContextException.class, () -> QueryContext.get());
    }

}
