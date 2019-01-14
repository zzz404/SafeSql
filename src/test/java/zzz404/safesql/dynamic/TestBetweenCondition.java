package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.SafeSqlException;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.type.TypedValue;

class TestBetweenCondition {
    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_sql_and_paramValues() {
        DbSource.create().useConnectionPrivider(() -> FakeDatabase.getDefaultconnection());
        DynamicQuerier q = from(Document.class).where(d -> {
            cond(d.getId(), BETWEEN, 123, 456);
        });
        assertEquals("SELECT * FROM Document t1 WHERE t1.id BETWEEN ? AND ?", q.sql());
        assertEquals(Arrays.asList(TypedValue.valueOf(123), TypedValue.valueOf(456)), q.paramValues());
    }

    @Test
    void test_constructor_errorParams_throwException() {
        DbSource.create().useConnectionPrivider(() -> FakeDatabase.getDefaultconnection());
        SafeSqlException e = assertThrows(SafeSqlException.class, () -> from(Document.class).where(d -> {
            cond(d.getId(), BETWEEN, 123);
        }));
        String msg = e.getMessage();
        assertTrue(msg.contains(" '" + BETWEEN + "'"));
        assertTrue(msg.contains(" 2"));
    }

    @Test
    void cover_rest() {
        BetweenCondition cond1 = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        cond1.toString();

        BetweenCondition cond2 = new BetweenCondition(UtilsForTest.createSimpleField("zzz"), 123, 456);
        assertEquals(cond1, cond2);
    }
}
