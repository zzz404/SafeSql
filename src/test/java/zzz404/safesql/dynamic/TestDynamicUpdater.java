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
    private int id = 1;
    private int categoryId = 2;
    private int ownerId = 3;
    private String title = "zzz";

    private Document doc;

    @BeforeEach
    void beforeEach() {
        this.fakeDb = new FakeDatabase().addTableColumns("Document", "id", "ownerId", "title", "abc");

        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
        });
        doc = new Document();
        doc.setId(id);
        doc.setCategoryId(categoryId);
        doc.setOwnerId(ownerId);
        doc.setTitle(title);
    }

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_update_hasParams() throws SQLException {
        String sql = "UPDATE Document SET title = ?,  FROM Document WHERE id = ? AND ownerId = ?";

        Connection conn = fakeDb.addTables("Document").getMockedConnection();

        update(doc);
        update(Document.class).collectFields(d -> {
            d.getTitle();
        }).where(d -> {
            cond(d.getId(), "=", 11);
        }).execute();

        verify(conn, times(1)).prepareStatement(sql);

        PreparedStatement pstmt = conn.prepareStatement(sql);
        verify(pstmt, times(1)).setInt(1, 11);
        verify(pstmt, times(1)).setInt(2, 2);
    }

}
