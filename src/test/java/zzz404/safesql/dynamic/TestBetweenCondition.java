package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeSchemaBase;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.type.TypedValue;

class TestBetweenCondition {
    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_toClause() {
        DbSource.create().useConnectionPrivider(() -> FakeSchemaBase.getDefaultconnection());
        from(Document.class).where(d -> {
            cond(d.getId(), BETWEEN, 123, 456);
            Arrays.asList(1, "aa");
        });
        BetweenCondition cond = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        assertEquals("t1.zzz BETWEEN ? AND ?", cond.toClause());
    }

    @Test
    void test_appendValuesTo() {
        BetweenCondition cond = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        ArrayList<TypedValue<?>> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(Arrays.asList(TypedValue.valueOf(123), TypedValue.valueOf(456)), values);
    }

    @Test
    void cover_rest() {
        BetweenCondition cond1 = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        cond1.toString();

        BetweenCondition cond2 = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        assertEquals(cond1, cond2);
    }
}
