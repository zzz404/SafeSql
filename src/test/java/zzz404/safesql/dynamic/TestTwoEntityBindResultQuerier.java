package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.helper.DocCount;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.Record;
import zzz404.safesql.helper.User;

public class TestTwoEntityBindResultQuerier {

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_select_where_groupBy_orderBy() throws SQLException {
        String sql = "SELECT t2.name AS ownerName, COUNT(*) AS count FROM Document t1, User t2"
                + " WHERE t1.ownerId = t2.id GROUP BY t2.name ORDER BY t2.name ASC";
        FakeDatabase fakeDb = new FakeDatabase().addTables("Document", "User").addRecords(sql,
                new Record().setValue("ownerName", "zzz").setValue("count", 1));
        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
        });

        TwoEntityBindResultQuerier<Document, User, DocCount> querier = from(Document.class, User.class)
                .to(DocCount.class).select((d, u, dc) -> {
                    field(u.getName()).as(dc.getOwnerName());
                    count().as(dc.getCount());
                }).where((d, u) -> cond(d.getOwnerId(), "=", u.getId())).groupBy((d, u) -> u.getName())
                .orderBy((d, u) -> asc(u.getName()));

        DocCount docCount = querier.queryOne().get();

        assertEquals("zzz", docCount.getOwnerName());
        assertEquals(1, docCount.getCount());
    }

}
