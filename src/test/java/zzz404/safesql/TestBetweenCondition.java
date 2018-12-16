package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;

class TestBetweenCondition {

    @Test
    void test_toClause() {
        BetweenCondition cond = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        assertEquals("zzz BETWEEN ? AND ?", cond.toClause());
    }

    @Test
    void test_appendValuesTo() {
        BetweenCondition cond = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        ArrayList<Object> values = new ArrayList<>();
        cond.appendValuesTo(values);
        assertEquals(2, values.size());
        assertEquals(123, values.get(0));
        assertEquals(456, values.get(1));
    }

    @Test
    void cover_rest() {
        BetweenCondition cond1 = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        cond1.toString();

        BetweenCondition cond2 = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        assertEquals(cond1, cond2);
    }
}
