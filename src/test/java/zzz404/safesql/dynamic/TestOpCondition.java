package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.type.TypedValue;

class TestOpCondition {

    private static final FieldImpl column_zzz = UtilsForTest.createSimpleField("zzz");

    @Test
    void test_toClause() {
        OpCondition cond = new OpCondition(column_zzz, "=", 123);
        assertEquals("t1.zzz = ?", cond.toClause());
    }

    @Test
    void test_appendValuesTo() {
        OpCondition cond = new OpCondition(column_zzz, "=", 123);

        ArrayList<TypedValue<?>> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(UtilsForTest.createTypedValueList(123), values);
    }

    @Test
    void cover_rest() {
        new OpCondition(column_zzz, "", 1).toString();
    }
}
