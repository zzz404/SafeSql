package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;

class TestInCondition {

    private static final Field column_zzz = UtilsForTest.createSimpleField("zzz");

    @Test
    void test_toClause() {
        InCondition cond = new InCondition(column_zzz, 1, 2, 3);
        assertEquals("zzz IN (?, ?, ?)", cond.toClause());
    }

    @Test
    void test_toClause_noParams() {
        InCondition cond = new InCondition(column_zzz);
        assertEquals("0<>0", cond.toClause());
    }

    @Test
    void test_appendValuesTo() throws SQLException {
        InCondition cond = new InCondition(column_zzz, 1, 3, 7);

        ArrayList<Object> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(Arrays.asList(1, 3, 7), values);
    }

    @Test
    void cover_rest() {
        new InCondition(UtilsForTest.createSimpleField(""), "", "").toString();
    }
}
