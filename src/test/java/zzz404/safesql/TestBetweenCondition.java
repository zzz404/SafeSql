package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.type.TypedValue;

class TestBetweenCondition {

    @Test
    void test_toClause() {
        BetweenCondition<Integer> cond = new BetweenCondition<>(UtilsForTest.createSimpleField("zzz"), 123, 456);
        assertEquals("t1.zzz BETWEEN ? AND ?", cond.toClause());
    }

    @Test
    void test_appendValuesTo() {
        BetweenCondition<Integer> cond = new BetweenCondition<>(UtilsForTest.createSimpleField("zzz"), 123, 456);
        ArrayList<TypedValue<?>> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(Arrays.asList(TypedValue.valueOf(123), TypedValue.valueOf(456)), values);
    }

    @Test
    void cover_rest() {
        BetweenCondition<Integer> cond1 = new BetweenCondition<>(UtilsForTest.createSimpleField("zzz"), 123, 456);
        cond1.toString();

        BetweenCondition<Integer> cond2 = new BetweenCondition<>(UtilsForTest.createSimpleField("zzz"), 123, 456);
        assertEquals(cond1, cond2);
    }
}
