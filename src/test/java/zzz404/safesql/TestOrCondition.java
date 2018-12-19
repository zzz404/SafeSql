package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.QuerierBackDoor;

class TestOrCondition {

    private static OpCondition cond1 = new OpCondition(UtilsForTest.createSimpleField("a"), "=", "aaa");
    private static OpCondition cond2 = new OpCondition(UtilsForTest.createSimpleField("b"), "<>", 11);

    @Test
    void test_toClause() {
        OrCondition cond = new OrCondition(cond1, cond2);
        assertEquals("(t1.a = ? OR t1.b <> ?)", cond.toClause());
    }

    @Test
    void test_appendValuesTo() throws SQLException {
        OrCondition cond = new OrCondition(cond1, cond2);

        ArrayList<Object> values = new ArrayList<>();
        cond.appendValuesTo(values);

        assertEquals(Arrays.asList("aaa", 11), values);
    }

    @Test
    void test_or() throws SQLException {
        Connection conn = new FakeDatabase().getMockedConnection();
        DbSource.create().useConnectionPrivider(() -> conn);
        try {
            OneEntityQuerier<Document> querier = from(Document.class).where(doc -> {
                cond(doc.getOwnerId(), "=", 111);
                cond(doc.getId(), "<", 1).or(doc.getId(), ">", 100).or(doc.getTitle(), LIKE, "zzz%");
            });
            assertEquals(
                    "SELECT * FROM Document t1 WHERE t1.ownerId = ? AND (t1.id < ? OR t1.id > ? OR t1.title LIKE ?)",
                    QuerierBackDoor.sql(querier));
            UtilsForTest.assertEquals(Arrays.asList(111, 1, 100, "zzz%"), QuerierBackDoor.paramValues(querier));
        }
        finally {
            DbSource.map.clear();
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
