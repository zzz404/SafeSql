package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.DbSource;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.Page;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.Record;
import zzz404.safesql.util.Tuple2;

class TestOneEntityQuerier {

    private String sql = "SELECT t1.id, t1.title FROM Document t1 WHERE t1.id < ? ORDER BY t1.id DESC";
    private String sql_for_count = "SELECT COUNT(*) FROM Document t1 WHERE t1.id < ?";
    private FakeDatabase fakeDb;
    
    @AfterEach
    void afterEach() {
        fakeDb = null;
        DbSourceBackDoor.removeAllFactories();
    }

    private void registerDefaultConnectionProvider() throws SQLException {
        fakeDb = new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId")
                .addSingleColumnValues(sql_for_count, "", 4)
                .addSingleColumnValues(sql, "title", "aaa", "bbb", "ccc", "ddd");
        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
        });
    }

    private OneEntityQuerier<Document> createQuerier() {
        return from(Document.class).select(d -> {
            d.getId();
            d.getTitle();
        }).where(d -> {
            cond(d.getId(), "<", 100);
        }).orderBy(d -> {
            desc(d.getId());
        }).offset(1).limit(2);
    }

    @Test
    void test_queryOne() throws SQLException {
        registerDefaultConnectionProvider();

        OneEntityQuerier<Document> q = createQuerier();
        Document doc = q.queryOne().get();
        assertEquals("bbb", doc.getTitle());
        fakeDb.assertParamValues(sql, 100);
    }

    @Test
    void test_queryList() throws SQLException {
        registerDefaultConnectionProvider();

        List<String> titles = createQuerier().queryList().stream().map(Document::getTitle).collect(Collectors.toList());
        assertEquals(Arrays.asList("bbb", "ccc"), titles);
    }

    @Test
    void test_queryPage() throws SQLException {
        registerDefaultConnectionProvider();

        Page<Document> page = createQuerier().queryPage();
        assertEquals(4, page.getTotalCount());
        assertEquals(Arrays.asList("bbb", "ccc"),
                page.getResult().stream().map(Document::getTitle).collect(Collectors.toList()));
        
        fakeDb.assertParamValues(sql, 100);
        fakeDb.assertParamValues(sql_for_count, 100);
    }

    @Test
    void test_queryEntityStream() throws SQLException {
        registerDefaultConnectionProvider();

        List<String> titles = createQuerier().queryEntityStream(stream -> {
            return stream.map(Document::getTitle).collect(Collectors.toList());
        });
        assertEquals(Arrays.asList("bbb", "ccc"), titles);
    }

    @Test
    void test_groupBy() throws SQLException {
        String sql = "SELECT t1.ownerId, COUNT(*) FROM Document t1 WHERE t1.id < ? GROUP BY t1.ownerId ORDER BY t1.ownerId ASC";
        FakeDatabase fakeDb = new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId")
                .addRecords(sql, new Record().setValue("Document", "ownerId", 1).setValue("count", 2),
                        new Record().setValue("ownerId", 3).setValue("count", 4));
        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb
                    .getMockedConnection();
        });

        OneEntityQuerier<Document> querier = from(Document.class).select(d -> {
            d.getOwnerId();
            count();
        }).where(d -> {
            cond(d.getId(), "<", 100);
        }).groupBy(d -> {
            d.getOwnerId();
        }).orderBy(d -> {
            asc(d.getOwnerId());
        });
        List<Tuple2<Integer, Integer>> tuples = querier.queryStream(stream -> {
            return stream.map(rs -> {
                int ownerId = rs.getInt(1);
                int count = rs.getInt(2);
                return new Tuple2<>(ownerId, count);
            }).collect(Collectors.toList());
        });

        fakeDb.assertParamValues(sql, 100);
        assertEquals(Arrays.asList(new Tuple2<>(1, 2), new Tuple2<>(3, 4)), tuples);
    }

}
