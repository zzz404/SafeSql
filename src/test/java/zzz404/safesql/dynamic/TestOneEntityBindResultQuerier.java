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

public class TestOneEntityBindResultQuerier {

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_select_where_groupBy_orderBy() throws SQLException {
        String sql = "SELECT t1.ownerId, COUNT(*) AS count FROM Document t1 WHERE t1.id < ? GROUP BY t1.ownerId ORDER BY t1.ownerId ASC";
        FakeDatabase fakeDb = new FakeDatabase().addTableColumns("Document", "ownerId")
                .addRecords(sql, new Record().setValue("ownerId", 12).setValue("count", 3));
        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
        });

        OneEntityBindResultQuerier<Document, DocCount> querier = from(Document.class).to(DocCount.class)
                .select((d, dc) -> {
                    d.getOwnerId();
                    count().as(dc.getCount());
                }).where(d -> cond(d.getId(), "<", 1000)).groupBy(d -> d.getOwnerId())
                .orderBy(d -> asc(d.getOwnerId()));

        DocCount docCount = querier.queryOne().get();
        
        fakeDb.assertParamValues(sql, 1000);

        assertEquals(12, docCount.getOwnerId());
        assertEquals(3, docCount.getCount());
    }

}
