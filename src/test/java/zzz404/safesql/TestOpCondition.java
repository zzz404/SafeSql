package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TestOpCondition {

    private static final TableField column_zzz = new TableField(0, "zzz");

    @Test
    void test_toClause() {
        OpCondition cond = new OpCondition(column_zzz, "=", 123);
        assertEquals("zzz = ?", cond.toClause());
    }

    @Test
    void test_appendValuesTo() {
        OpCondition cond = new OpCondition(column_zzz, "=", 123);
        
        ArrayList<Object> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(Arrays.asList(123), values);
    }

    @Test
    void coverRest() {
        new OpCondition(column_zzz, "", "").toString();
    }
}
