package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.dynamic.FieldImpl;
import zzz404.safesql.dynamic.OneEntityQuerier;
import zzz404.safesql.dynamic.OpCondition;
import zzz404.safesql.dynamic.OrCondition;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.SqlQuerierBackDoor;
import zzz404.safesql.sql.type.TypedValue;

class TestOrCondition {

    private static OpCondition<String> cond1 = new OpCondition<>(new FieldImpl<>(new Entity<>(1, Object.class), "a"), "=",
            "aaa");
    private static OpCondition<Integer> cond2 = new OpCondition<>(new FieldImpl<>(new Entity<>(2, Object.class), "b"), "<>",
            11);

    @Test
    void test_toClause() {
        OrCondition cond = new OrCondition(cond1, cond2);
        assertEquals("(t1.a = ? OR t2.b <> ?)", cond.toClause());
    }

    @Test
    void test_appendValuesTo() throws SQLException {
        OrCondition cond = new OrCondition(cond1, cond2);

        ArrayList<TypedValue<?>> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(UtilsForTest.createTypedValueList("aaa", 11), values);
    }

    @Test
    void test_or() throws SQLException {
        Connection conn = new FakeDatabase().getMockedConnection();
        DbSource.create().useConnectionPrivider(() -> conn);
        try {
            OneEntityQuerier<Document> querier = from(Document.class).where(doc -> {
                cond(1, "=", 2);
                cond(doc.getOwnerId(), "=", 111);
                cond(doc.getId(), "<", 1).or(doc.getId(), ">", 100).or(doc.getTitle(), LIKE, "zzz%");
            });
            assertEquals(
                    "SELECT * FROM Document t1 WHERE t1.ownerId = ? AND (t1.id < ? OR t1.id > ? OR t1.title LIKE ?)",
                    SqlQuerierBackDoor.sql(querier));
            assertEquals(UtilsForTest.createTypedValueList(111, 1, 100, "zzz%"),
                    SqlQuerierBackDoor.paramValues(querier));
        }
        finally {
            DbSourceBackDoor.removeAllFactories();
        }
    }

    @Test
    void cover_rest() {
        OrCondition orCond = new OrCondition(cond1, cond2);
        orCond.toString();
        OrCondition orCond2 = new OrCondition(cond1, cond2);
        orCond.equals(orCond2);
    }
}
