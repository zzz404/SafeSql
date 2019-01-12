package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.FieldImpl;
import zzz404.safesql.dynamic.InCondition;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.type.TypedValue;

class TestInCondition {

    private static final FieldImpl column_zzz = UtilsForTest.createSimpleField("zzz");

    @Test
    void test_toClause() {
        InCondition cond = new InCondition(column_zzz, 1, 2, 3);
        assertEquals("t1.zzz IN (?, ?, ?)", cond.toClause());
    }

    @Test
    void test_toClause_noParams() {
        InCondition cond = new InCondition(column_zzz);
        assertEquals("0<>0", cond.toClause());
    }

    @Test
    void test_appendValuesTo() throws SQLException {
        InCondition cond = new InCondition(column_zzz, 1, 3, 7);

        ArrayList<TypedValue<?>> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(UtilsForTest.createTypedValueList(1, 3, 7), values);
    }

    @Test
    void cover_rest() {
        InCondition cond = new InCondition(UtilsForTest.createSimpleField(""), 1);
        cond.toString();
        InCondition cond2 = new InCondition(UtilsForTest.createSimpleField(""), 1);
        cond.equals(cond2);
    }
}
