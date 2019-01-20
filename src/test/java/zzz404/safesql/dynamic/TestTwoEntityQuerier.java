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
import zzz404.safesql.helper.User;
import zzz404.safesql.util.Tuple2;

class TestTwoEntityQuerier {

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    private void registerDefaultConnectionProvider() {
        String sql = "SELECT t1.title, t2.name FROM Document t1, User t2 WHERE t1.ownerId = t2.id ORDER BY t1.id ASC";
        String sql_for_count = "SELECT COUNT(*) FROM Document t1, User t2 WHERE t1.ownerId = t2.id";
        DbSource.create().useConnectionPrivider(() -> {
            return new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId")
                    .addTableColumns("User", "id", "name").addSingleColumnValues(sql_for_count, "", 4)
                    .addRecords(sql,
                            new Record().setValue("Document", "title", "title1").setValue("User", "name", "name1"),
                            new Record().setValue("Document", "title", "title2").setValue("User", "name", "name2"),
                            new Record().setValue("Document", "title", "title3").setValue("User", "name", "name3"),
                            new Record().setValue("Document", "title", "title4").setValue("User", "name", "name4"))
                    .getMockedConnection();
        });
    }

    private TwoEntityQuerier<Document, User> createQuerier() {
        return from(Document.class, User.class).select((d, u) -> {
            d.getTitle();
            u.getName();
        }).where((d, u) -> {
            cond(d.getOwnerId(), "=", u.getId());
        }).orderBy((d, u) -> {
            asc(d.getId());
        }).offset(1).limit(2);
    }

    @Test
    void test_queryOne() {
        registerDefaultConnectionProvider();

        Tuple2<Document, User> docUser = createQuerier().queryOne().get();
        assertEquals("title2", docUser.first().getTitle());
        assertEquals("name2", docUser.second().getName());
    }

    @Test
    void test_queryList() {
        registerDefaultConnectionProvider();

        List<Tuple2<String, String>> tuples = createQuerier().queryList().stream().map(docUser -> {
            return new Tuple2<>(docUser.first().getTitle(), docUser.second().getName());
        }).collect(Collectors.toList());
        assertEquals(Arrays.asList(new Tuple2<>("title2", "name2"), new Tuple2<>("title3", "name3")), tuples);
    }

    @Test
    void test_queryPage() {
        registerDefaultConnectionProvider();
        Page<Tuple2<Document, User>> page = createQuerier().queryPage();
        assertEquals(4, page.getTotalCount());
        assertEquals(Arrays.asList(new Tuple2<>("title2", "name2"), new Tuple2<>("title3", "name3")),
                page.getResult().stream().map(docUser -> {
                    return new Tuple2<>(docUser.first().getTitle(), docUser.second().getName());
                }).collect(Collectors.toList()));
    }

    @Test
    void test_queryEntityStream() {
        registerDefaultConnectionProvider();

        List<Tuple2<String, String>> tuples = createQuerier().queryEntitiesStream(stream -> {
            return stream.map(docUser -> {
                return new Tuple2<>(docUser.first().getTitle(), docUser.second().getName());
            }).collect(Collectors.toList());
        });
        assertEquals(Arrays.asList(new Tuple2<>("title2", "name2"), new Tuple2<>("title3", "name3")), tuples);
    }

    @Test
    void test_groupBy() {
        String sql = "SELECT t2.name, COUNT(*) FROM Document t1, User t2"
                + " WHERE t1.ownerId = t2.id GROUP BY t2.name ORDER BY t2.name ASC";
        DbSource.create().useConnectionPrivider(() -> {
            return new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId")
                    .addTableColumns("User", "id", "name")
                    .addRecords(sql, new Record().setValue("name", "aaa").setValue("count", 1),
                            new Record().setValue("name", "ccc").setValue("count", 3))
                    .getMockedConnection();
        });

        TwoEntityQuerier<Document, User> querier = from(Document.class, User.class).select((d, u) -> {
            u.getName();
            count();
        }).where((d, u) -> {
            cond(d.getOwnerId(), "=", u.getId());
        }).groupBy((d, u) -> {
            u.getName();
        }).orderBy((d, u) -> {
            asc(u.getName());
        });
        List<Tuple2<String, Integer>> tuples = querier.queryList_by_mapEach(rs -> {
            String name = rs.getString(1);
            int count = rs.getInt(2);
            return new Tuple2<>(name, count);
        });
        assertEquals(Arrays.asList(new Tuple2<>("aaa", 1), new Tuple2<>("ccc", 3)), tuples);
    }

}
