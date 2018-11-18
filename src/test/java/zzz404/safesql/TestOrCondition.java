package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TestOrCondition {

    private static OpCondition cond1 = new OpCondition(new TableColumn(0, "a"), "=", "aaa");
    private static OpCondition cond2 = new OpCondition(new TableColumn(0, "b"), "<>", 11);

    @Test
    void test_toClause() {
        OrCondition cond = new OrCondition(cond1, cond2);
        assertEquals("(a = ? OR b <> ?)", cond.toClause());
    }

    @Test
    void test_appendValuesTo() throws SQLException {
        OrCondition cond = new OrCondition(cond1, cond2);

        ArrayList<Object> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(Arrays.asList("aaa", 11), values);
    }

    @Test
    void coverRest() {
        OrCondition orCond = new OrCondition(cond1, cond2);
        orCond.toString();
        orCond.equals(orCond);
    }
}
