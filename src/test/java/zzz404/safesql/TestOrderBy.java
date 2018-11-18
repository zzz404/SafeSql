package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class TestOrderBy {
    @Test
    void test_toClause_asc() {
        OrderBy orderBy = new OrderBy(new TableColumn(0, "aa"), true);
        assertEquals("aa ASC", orderBy.toClause());

        orderBy = new OrderBy(new TableColumn(0, "bb"), false);
        assertEquals("bb DESC", orderBy.toClause());
    }

    @Test
    void coverRest() {
        new OrderBy(new TableColumn(0, ""), false).toString();
    }
}
