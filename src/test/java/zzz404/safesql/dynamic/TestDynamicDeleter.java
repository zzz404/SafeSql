package zzz404.safesql.dynamic;

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
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;

class TestDynamicDeleter {

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
    void test_delete_hasParams() throws SQLException {
        String sql = "DELETE FROM Document WHERE id = ? AND ownerId = ?";

        Connection conn = fakeDb.addTables("Document").getMockedConnection();

        delete(Document.class).where(d -> {
            cond(d.getId(), "=", 11);
            cond(d.getOwnerId(), "=", 2);
        }).execute();

        verify(conn, times(1)).prepareStatement(sql);

        PreparedStatement pstmt = conn.prepareStatement(sql);
        verify(pstmt).setInt(1, 11);
        verify(pstmt).setInt(2, 2);
        verify(pstmt).executeUpdate();
    }

    @Test
    void test_delete_noParams() throws SQLException {
        String sql = "DELETE FROM Document";

        Connection conn = fakeDb.addTables("Document").getMockedConnection();

        delete(Document.class).execute();

        verify(conn, times(2)).createStatement(); // query table 用了一次

        Statement stmt = conn.createStatement();
        verify(stmt).executeUpdate(sql);
    }

}
