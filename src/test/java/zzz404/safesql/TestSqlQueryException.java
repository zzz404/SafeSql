package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.SafeSql.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.OneEntityQuerier;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.UtilsForTest;

public class TestSqlQueryException {
    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_throw() throws SQLException {
        Connection conn = new FakeDatabase().addTableColumns("Document").getMockedConnection();
        RuntimeException re = new RuntimeException();
        when(conn.prepareStatement("SELECT * FROM Document t1 WHERE t1.id > ?")).thenThrow(re);
        DbSource.create().useConnectionPrivider(() -> conn);

        OneEntityQuerier<Document> querier = from(Document.class).where(d -> {
            cond(d.getId(), ">", "ccc");
        });
        SqlQueryException e = assertThrows(SqlQueryException.class, () -> querier.queryList());
        assertEquals("SELECT * FROM Document t1 WHERE t1.id > ?", e.getSql());
        assertEquals(UtilsForTest.createTypedValueList("ccc"), e.getParamValues());
        assertEquals("SELECT * FROM Document t1 WHERE t1.id > 'ccc'", e.getValuedSql());
        assertEquals("Error on query : SELECT * FROM Document t1 WHERE t1.id > 'ccc'",
                e.getMessage());
        assertEquals(re, e.getCause());
    }
}
