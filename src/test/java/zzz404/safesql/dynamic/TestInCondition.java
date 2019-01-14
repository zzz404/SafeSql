package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.type.TypedValue;

class TestInCondition {

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_sql_and_paramValues__three_params() {
        DbSource.create().useConnectionPrivider(() -> FakeDatabase.getDefaultconnection());
        DynamicQuerier q = from(Document.class).where(d -> {
            cond(d.getId(), IN, 123, 456, 789);
        });
        assertEquals("SELECT * FROM Document t1 WHERE t1.id IN (?, ?, ?)", q.sql());
        assertEquals(Arrays.asList(TypedValue.valueOf(123), TypedValue.valueOf(456), TypedValue.valueOf(789)),
                q.paramValues());
    }

    @Test
    void test_sql_and_paramValues__one_params() {
        DbSource.create().useConnectionPrivider(() -> FakeDatabase.getDefaultconnection());
        DynamicQuerier q = from(Document.class).where(d -> {
            cond(d.getId(), IN, 123);
        });
        assertEquals("SELECT * FROM Document t1 WHERE t1.id IN (?)", q.sql());
        assertEquals(Arrays.asList(TypedValue.valueOf(123)), q.paramValues());
    }

    @Test
    void test_sql_and_paramValues__no_params() {
        DbSource.create().useConnectionPrivider(() -> FakeDatabase.getDefaultconnection());
        DynamicQuerier q = from(Document.class).where(d -> {
            cond(d.getId(), IN);
        });
        assertEquals("SELECT * FROM Document t1 WHERE 0<>0", q.sql());
        assertEquals(Arrays.asList(), q.paramValues());
    }

    @Test
    void cover_rest() {
        InCondition cond = new InCondition(UtilsForTest.createSimpleField(""), 1);
        cond.toString();
        InCondition cond2 = new InCondition(UtilsForTest.createSimpleField(""), 1);
        cond.equals(cond2);
    }
}
