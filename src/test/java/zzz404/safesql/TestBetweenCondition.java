package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TestBetweenCondition {

    @Test
    void test_toClause() {
        BetweenCondition cond = new BetweenCondition(new TableColumn(0, "zzz"), 123, 456);
        assertEquals("zzz BETWEEN ? AND ?", cond.toClause());
    }

    @Test
    void test_appendValuesTo() {
        BetweenCondition cond = new BetweenCondition(new TableColumn(0, "zzz"), 123, 456);
        ArrayList<Object> values = new ArrayList<>();
        cond.appendValuesTo(values);
        assertEquals(2, values.size());
        assertEquals(123, values.get(0));
        assertEquals(456, values.get(1));
    }

    @Test
    void coverRest() {
        new BetweenCondition(new TableColumn(0, "zzz"), 123, 456).toString();
    }
}
