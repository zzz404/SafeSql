package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.Sql.*;

import java.sql.Connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.OneEntityQuerier;
import zzz404.safesql.helper.Document;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.SqlQuerierBackDoor;
import zzz404.safesql.sql.proxy.EnhancedConnection;

public class TestSql {

    @AfterEach
    void afterEach() {
        DbSource.clearAll();
    }

    @Test
    void test_use() {
        assertThrows(RuntimeException.class, () -> use("aaa"));

        DbSource.create("bbb");
        assertThrows(RuntimeException.class, () -> use("aaa"));

        DbSource ds = DbSource.create("aaa");
        QuerierFactory qf = use("aaa");
        assertEquals(ds, qf.dbSource);
    }

    @Test
    void test_useDefault() {
        assertThrows(RuntimeException.class, () -> use());

        DbSource ds = DbSource.create();
        QuerierFactory qf = use();
        assertEquals(ds, qf.dbSource);
    }

    @Test
    void test_sql_useDefault() {
        assertThrows(RuntimeException.class, () -> sql("hi"));

        DbSource ds = DbSource.create("");
        assertEquals(ds, SqlQuerierBackDoor.getDbSource(sql("hi")));
    }

    @Test
    void test_from_useDefault() {
        assertThrows(RuntimeException.class, () -> from(Object.class));

        DbSource ds = DbSource.create("");
        assertEquals(ds, SqlQuerierBackDoor.getDbSource(from(Object.class)));
    }

    @Test
    void test_withTheSameConnection_hasCorrectDbSource() {
        Connection conn = mock(Connection.class);
        DbSource.create("z").useConnectionPrivider(() -> conn);

        DbSourceImpl ds = DbSourceImpl.get("z");

        use("z").withTheSameConnection(() -> {
            OneEntityQuerier<Document> querier1 = use("z").from(Document.class);
            DbSourceImpl ds1 = SqlQuerierBackDoor.getDbSource(querier1);
            assertEquals(ds, ds1);
            return null;
        });
    }

    @Test
    void test_withConnection_useErrorDataSource_throwException() {
        Connection conn = mock(Connection.class);
        DbSource.create("z1").useConnectionPrivider(() -> conn);
        DbSource.create("z2").useConnectionPrivider(() -> conn);

        assertThrows(IllegalArgumentException.class, () -> {
            use("z1").withTheSameConnection(() -> {
                DbSourceContext.withConnection(DbSourceImpl.get("z2"), enConn -> null);
                return null;
            });
        });
    }

    @Test
    void test_withTheSameConnection_hasCorrectConnection() {
        DbSource.create("z").useConnectionPrivider(() -> mock(Connection.class));
        DbSourceImpl ds = DbSourceImpl.get("z");

        EnhancedConnection conn1 = DbSourceContext.withConnection(ds, conn -> conn);
        EnhancedConnection conn2 = DbSourceContext.withConnection(ds, conn -> conn);

        assertNotEquals(conn1, conn2);

        use("z").withTheSameConnection(() -> {
            EnhancedConnection conn3 = DbSourceContext.withConnection(ds, conn -> conn);
            EnhancedConnection conn4 = DbSourceContext.withConnection(ds, conn -> conn);
            assertEquals(conn3, conn4);
            return null;
        });
    }

}
