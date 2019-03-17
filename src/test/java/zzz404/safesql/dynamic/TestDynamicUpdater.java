package zzz404.safesql.dynamic;

import static org.mockito.Mockito.*;
import static zzz404.safesql.SafeSql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;

class TestDynamicUpdater {

    private FakeDatabase fakeDb;

    @BeforeEach
    void beforeEach() throws SQLException {
        this.fakeDb = new FakeDatabase().addTableColumns("Document");

        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
        });
    }

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_update() throws SQLException {
        String sql = "UPDATE Document SET ownerId=?, title=? WHERE id <> ? AND categoryId = ?";

        Connection conn = fakeDb.getMockedConnection();

        update(Document.class).set(d -> {
            d.setOwnerId(12);
            d.setTitle("zzz");
        }).where(d -> {
            cond(d.getId(), "<>", 11);
            cond(d.getCategoryId(), "=", 22);
        }).execute();

        verify(conn).prepareStatement(sql);

        PreparedStatement pstmt = conn.prepareStatement(sql);
        verify(pstmt).setInt(1, 12);
        verify(pstmt).setString(2, "zzz");
        verify(pstmt).setInt(3, 11);
        verify(pstmt).setInt(4, 22);
    }

    @Test
    void test_update_nullValue() throws SQLException {
        String sql = "UPDATE Document SET ownerId=?, title=? WHERE ownerId = ? AND title = ?";

        Connection conn = fakeDb.getMockedConnection();

        update(Document.class).set(d -> {
            d.setOwnerId(null);
            d.setTitle(null);
        }).where(d -> {
            cond(d.getOwnerId(), "=", (Integer) null);
            cond(d.getTitle(), "=", (String) null);
        }).execute();

        verify(conn).prepareStatement(sql);

        PreparedStatement pstmt = conn.prepareStatement(sql);
        verify(pstmt).setObject(1, null);
        verify(pstmt).setString(2, null);
        verify(pstmt).setObject(3, null);
        verify(pstmt).setString(4, null);
    }

}
