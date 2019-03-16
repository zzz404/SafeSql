package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.SafeSql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.SafeSql;
import zzz404.safesql.helper.FakeDatabase;

class TestStaticSqlExecuter {

    private FakeDatabase fakeDb;

    @BeforeEach
    void beforeEach() {
        this.fakeDb = new FakeDatabase();
        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
        });
    }

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_update_hasParams() throws SQLException {
        Connection conn = fakeDb.getMockedConnection();

        sql("zzz").paramValues(3, "oo").update();
        verify(conn, times(1)).prepareStatement("zzz");

        PreparedStatement pstmt = conn.prepareStatement("zzz");
        verify(pstmt, times(1)).setInt(1, 3);
        verify(pstmt, times(1)).setString(2, "oo");
        verify(pstmt, times(1)).executeUpdate();
    }

    @Test
    void test_update_noParams() throws SQLException {
        Connection conn = fakeDb.getMockedConnection();

        SafeSql.sql("zzz").update();
        verify(conn, times(1)).createStatement();
        Statement stmt = conn.createStatement();
        verify(stmt, times(1)).executeUpdate("zzz");
    }

    @Test
    void test_query_hasParams() throws SQLException {
        Connection conn = fakeDb.addRecords("zzz").getMockedConnection();

        SafeSql.sql("zzz").paramValues(3, "oo").queryList(Integer.class);
        verify(conn, times(1)).prepareStatement("zzz");

        PreparedStatement pstmt = conn.prepareStatement("zzz");
        verify(pstmt, times(1)).setInt(1, 3);
        verify(pstmt, times(1)).setString(2, "oo");
        verify(pstmt, times(1)).executeQuery();
    }

    @Test
    void test_query_noParams() throws SQLException {
        Connection conn = fakeDb.addRecords("zzz").getMockedConnection();

        SafeSql.sql("zzz").queryList(Integer.class);
        verify(conn, times(1)).createStatement();
        Statement stmt = conn.createStatement();
        verify(stmt, times(1)).executeQuery("zzz");
    }

    @Test
    void test_sql_for_queryCount() {
        String sql_hasOrderBy = "select aaa,bbb, ccc FROM ddd where aaa=1 order BY eee";
        StaticSqlExecuter q = new StaticSqlExecuter(null).sql(sql_hasOrderBy);
        assertEquals("SELECT COUNT(*) FROM ddd where aaa=1", q.sql_for_queryCount());

        String sql_noOrderBy = "select aaa,bbb, ccc FROM ddd where aaa=1 ";
        q = new StaticSqlExecuter(null).sql(sql_noOrderBy);
        assertEquals("SELECT COUNT(*) FROM ddd where aaa=1 ", q.sql_for_queryCount());
    }

    @Test
    void cover_rest() {
        new StaticSqlExecuter(null).offset(0).limit(0);
    }
}
