package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.Sql.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.Category;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.User;
import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.QuerierBackDoor;
import zzz404.safesql.querier.ThreeEntityQuerier;
import zzz404.safesql.querier.TwoEntityQuerier;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.sql.EnhancedConnection;

public class TestSql {

    @AfterEach
    void afterEach() {
        DbSource.map.clear();
    }

    @Test
    void test_use() {
        assertThrows(RuntimeException.class, () -> use("aaa"));

        DbSource.create("bbb");
        assertThrows(RuntimeException.class, () -> use("aaa"));

        DbSource.create("aaa");
        QuerierFactory qf = use("aaa");
        assertEquals("aaa", qf.dbSource.name);
    }

    @Test
    void test_useDefault() {
        assertThrows(RuntimeException.class, () -> use());

        DbSource.create();
        QuerierFactory qf = use();
        assertEquals("", qf.dbSource.name);
    }

    @Test
    void test_sql_useDefault() {
        assertThrows(RuntimeException.class, () -> sql("hi"));

        DbSource.create("");
        assertEquals("", QuerierBackDoor.getDbSource(sql("hi")).name);
    }

    @Test
    void test_from_useDefault() {
        assertThrows(RuntimeException.class, () -> from(Object.class));

        DbSource.create("");
        assertEquals("", QuerierBackDoor.getDbSource(from(Object.class)).name);
    }

    @Test
    void test_count() throws SQLException {
        Connection conn = new FakeDatabase().getMockedConnection();
        DbSource.create().useConnectionPrivider(() -> conn);
        OneEntityQuerier<Document> querier = from(Document.class).select(d -> {
            count();
            d.getId();
        });
        assertEquals("SELECT COUNT(*), t1.id FROM Document t1", QuerierBackDoor.sql(querier));
    }

    @Test
    void test_all() throws SQLException {
        Connection conn = new FakeDatabase().getMockedConnection();
        DbSource.create().useConnectionPrivider(() -> conn);
        TwoEntityQuerier<Document, User> querier = from(Document.class, User.class).select((d, u) -> {
            all(d);
            u.getId();
        });
        assertEquals("SELECT t1.*, t2.id FROM Document t1, User t2", QuerierBackDoor.sql(querier));
    }

    @Test
    void test_asc_desc() throws SQLException {
        Connection conn = new FakeDatabase().getMockedConnection();
        DbSource.create().useConnectionPrivider(() -> conn);

        ThreeEntityQuerier<Document, User, Category> querier = from(Document.class, User.class, Category.class)
                .orderBy((d, u, c) -> {
                    asc(d.getId());
                    desc(c, "id");
                    asc(u, "name");
                });
        assertEquals("SELECT * FROM Document t1, User t2, Category t3 ORDER BY t1.id ASC, t3.id DESC, t2.name ASC",
                QuerierBackDoor.sql(querier));
    }

    @Test
    void test_withTheSameConnection_hasCorrectDbSource() {
        Connection conn = mock(Connection.class);
        DbSource.create().useConnectionPrivider(() -> conn);

        Sql.withTheSameConnection(() -> {
            OneEntityQuerier<Document> querier1 = from(Document.class);
            DbSourceImpl ds1 = QuerierBackDoor.getDbSource(querier1);
            assertEquals("", ds1.name);
            OneEntityQuerier<Document> querier2 = from(Document.class);
            DbSourceImpl ds2 = QuerierBackDoor.getDbSource(querier2);
            assertEquals("", ds2.name);
            assertEquals(ds1, ds2);
            return null;
        });
    }

    @Test
    void test_withTheSameConnection_hasCorrectConnection() {
        DbSource.create().useConnectionPrivider(() -> mock(Connection.class));
        DbSourceImpl ds = DbSource.get("");

        EnhancedConnection conn1 = DbSourceContext.withConnection(ds, conn -> conn);
        EnhancedConnection conn2 = DbSourceContext.withConnection(ds, conn -> conn);

        assertNotEquals(conn1, conn2);

        Sql.withTheSameConnection(() -> {
            EnhancedConnection conn3 = DbSourceContext.withConnection(ds, conn -> conn);
            EnhancedConnection conn4 = DbSourceContext.withConnection(ds, conn -> conn);
            assertEquals(conn3, conn4);
            return null;
        });
    }

    @Test
    void test_innerJoin_() {
        DbSource.create().useConnectionPrivider(() -> mock(Connection.class));
        TwoEntityQuerier<Document, User> querier = from(Document.class, User.class).where((d, u) -> {
            innerJoin(d.getOwnerId(), "=", u.getId());
        });
        String sql = QuerierBackDoor.sql(querier);
        assertEquals("SELECT * FROM Document t1, User t2 WHERE t1.ownerId = t2.id", sql);
    }

}
