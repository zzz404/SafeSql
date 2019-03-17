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

class TestDynamicInserter {

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
    void test_insert() throws SQLException {
        insert(Document.class).values(d -> {
            d.setOwnerId(12);
            d.setTitle("zzz");
        }).execute();

        String sql = "INSERT INTO Document (ownerId, title) VALUES (?, ?)";

        Connection conn = fakeDb.getMockedConnection();
        verify(conn).prepareStatement(sql);

        PreparedStatement pstmt = conn.prepareStatement(sql);
        verify(pstmt).setInt(1, 12);
        verify(pstmt).setString(2, "zzz");
    }

    @Test
    void test_insert_nullValue() throws SQLException {
        insert(Document.class).values(d -> {
            d.setOwnerId(null);
            d.setTitle(null);
        }).execute();

        String sql = "INSERT INTO Document (ownerId, title) VALUES (?, ?)";

        Connection conn = fakeDb.getMockedConnection();
        verify(conn).prepareStatement(sql);

        PreparedStatement pstmt = conn.prepareStatement(sql);
        verify(pstmt).setObject(1, null);
        verify(pstmt).setString(2, null);
    }

}
