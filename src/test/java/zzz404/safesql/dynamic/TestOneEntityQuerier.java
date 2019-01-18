package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.SafeSql.*;

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

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    private void registerDefaultConnectionProvider() {
        String sql = "SELECT t1.id, t1.title FROM Document t1 WHERE t1.id < ? ORDER BY t1.id DESC";
        String sql_for_count = "SELECT COUNT(*) FROM Document t1 WHERE t1.id < ?";
        DbSource.create().useConnectionPrivider(() -> {
            return new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId")
                    .addSingleColumnValues(sql_for_count, "", 4)
                    .addSingleColumnValues(sql, "title", "aaa", "bbb", "ccc", "ddd").getMockedConnection();
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
    void test_queryOne() {
        registerDefaultConnectionProvider();

        Document doc = createQuerier().queryOne().get();
        assertEquals("bbb", doc.getTitle());
    }

    @Test
    void test_queryList() {
        registerDefaultConnectionProvider();

        List<String> titles = createQuerier().queryList().stream().map(Document::getTitle).collect(Collectors.toList());
        assertEquals(Arrays.asList("bbb", "ccc"), titles);
    }

    @Test
    void test_queryPage() {
        registerDefaultConnectionProvider();

        Page<Document> page = createQuerier().queryPage();
        assertEquals(4, page.getTotalCount());
        assertEquals(Arrays.asList("bbb", "ccc"),
                page.getResult().stream().map(Document::getTitle).collect(Collectors.toList()));
    }

    @Test
    void test_queryEntityStream() {
        registerDefaultConnectionProvider();

        List<String> titles = createQuerier().queryEntityStream(stream -> {
            return stream.map(Document::getTitle).collect(Collectors.toList());
        });
        assertEquals(Arrays.asList("bbb", "ccc"), titles);
    }

    @Test
    void test_groupBy() {
        String sql = "SELECT t1.ownerId, COUNT(*) FROM Document t1 WHERE t1.id < ? GROUP BY t1.ownerId ORDER BY t1.ownerId ASC";
        DbSource.create().useConnectionPrivider(() -> {
            return new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId")
                    .addRecords(sql, new Record().setValue("Document", "ownerId", 1).setValue("count", 2),
                            new Record().setValue("ownerId", 3).setValue("count", 4))
                    .getMockedConnection();
        });

        List<Tuple2<Integer, Integer>> tuples = from(Document.class).select(d -> {
            d.getOwnerId();
            count();
        }).where(d -> {
            cond(d.getId(), "<", 100);
        }).groupBy(d -> {
            d.getOwnerId();
        }).orderBy(d -> {
            asc(d.getOwnerId());
        }).queryStream(stream -> {
            return stream.map(rs -> {
                int ownerId = rs.getInt(1);
                int count = rs.getInt(2);
                return new Tuple2<>(ownerId, count);
            }).collect(Collectors.toList());
        });
        assertEquals(Arrays.asList(new Tuple2<>(1, 2), new Tuple2<>(3, 4)), tuples);
    }

}
