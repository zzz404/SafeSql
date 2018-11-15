package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestOrderBy {
    @Test
    void test_toClause_asc() {
        OrderBy orderBy = new OrderBy("aa", true);
        assertEquals("aa ASC", orderBy.toClause());
        
        orderBy = new OrderBy("bb", false);
        assertEquals("bb DESC", orderBy.toClause());
    }
}
