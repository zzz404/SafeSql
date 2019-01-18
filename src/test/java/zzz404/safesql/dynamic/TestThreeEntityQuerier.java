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
import zzz404.safesql.helper.Category;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.Record;
import zzz404.safesql.helper.User;
import zzz404.safesql.util.Tuple2;
import zzz404.safesql.util.Tuple3;

class TestThreeEntityQuerier {

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    private void registerDefaultConnectionProvider() {
        String sql = "SELECT t1.title, t2.name, t3.title FROM Document t1, User t2, Category t3"
                + " WHERE t1.ownerId = t2.id AND t1.categoryId = t3.id AND t3.id = ? ORDER BY t1.id ASC";
        String sql_for_count = "SELECT COUNT(*) FROM Document t1, User t2, Category t3"
                + " WHERE t1.ownerId = t2.id AND t1.categoryId = t3.id AND t3.id = ?";
        DbSource.create().useConnectionPrivider(() -> {
            return new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId", "categoryId")
                    .addTableColumns("User", "id", "name").addTableColumns("Category", "id", "title")
                    .addSingleColumnValues(sql_for_count, "", 4)
                    .addRecords(sql,
                            new Record().setValue("Document", "title", "title1").setValue("User", "name", "name1")
                                    .setValue("Category", "title", "ct1"),
                            new Record().setValue("Document", "title", "title2").setValue("User", "name", "name2")
                                    .setValue("Category", "title", "ct2"),
                            new Record().setValue("Document", "title", "title3").setValue("User", "name", "name3")
                                    .setValue("Category", "title", "ct3"),
                            new Record().setValue("Document", "title", "title4").setValue("User", "name", "name4")
                                    .setValue("Category", "title", "ct4"))
                    .getMockedConnection();
        });
    }

    private ThreeEntityQuerier<Document, User, Category> createQuerier() {
        return from(Document.class, User.class, Category.class).select((d, u, c) -> {
            d.getTitle();
            u.getName();
            c.getTitle();
        }).where((d, u, c) -> {
            cond(d.getOwnerId(), "=", u.getId());
            cond(d.getCategoryId(), "=", c.getId());
            cond(c.getId(), "=", 123);
        }).orderBy((d, u, c) -> {
            asc(d.getId());
        }).offset(1).limit(2);
    }

    @Test
    void test_queryOne() {
        registerDefaultConnectionProvider();

        Tuple3<Document, User, Category> duc = createQuerier().queryOne().get();
        assertEquals("title2", duc.first().getTitle());
        assertEquals("name2", duc.second().getName());
        assertEquals("ct2", duc.third().getTitle());
    }

    @Test
    void test_queryList() {
        registerDefaultConnectionProvider();

        List<Tuple3<String, String, String>> tuples = createQuerier().queryList().stream().map(duc -> {
            return new Tuple3<>(duc.first().getTitle(), duc.second().getName(), duc.third().getTitle());
        }).collect(Collectors.toList());
        assertEquals(Arrays.asList(new Tuple3<>("title2", "name2", "ct2"), new Tuple3<>("title3", "name3", "ct3")),
                tuples);
    }

    @Test
    void test_queryPage() {
        registerDefaultConnectionProvider();

        Page<Tuple3<Document, User, Category>> page = createQuerier().queryPage();
        assertEquals(4, page.getTotalCount());
        assertEquals(Arrays.asList(new Tuple3<>("title2", "name2", "ct2"), new Tuple3<>("title3", "name3", "ct3")),
                page.getResult().stream().map(duc -> {
                    return new Tuple3<>(duc.first().getTitle(), duc.second().getName(), duc.third().getTitle());
                }).collect(Collectors.toList()));
    }

    @Test
    void test_queryEntityStream() {
        registerDefaultConnectionProvider();

        List<Tuple3<String, String, String>> tuples = createQuerier().queryEntitiesStream(stream -> {
            return stream.map(duc -> {
                return new Tuple3<>(duc.first().getTitle(), duc.second().getName(), duc.third().getTitle());
            }).collect(Collectors.toList());
        });
        assertEquals(Arrays.asList(new Tuple3<>("title2", "name2", "ct2"), new Tuple3<>("title3", "name3", "ct3")),
                tuples);
    }

    @Test
    void test_groupBy() {
        String sql = "SELECT t2.name, COUNT(*) FROM Document t1, User t2, Category t3"
                + " WHERE t1.ownerId = t2.id AND t1.categoryId = t3.id GROUP BY t2.name ORDER BY t2.name ASC";
        DbSource.create().useConnectionPrivider(() -> {
            return new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId")
                    .addTableColumns("User", "id", "name").addTableColumns("Category", "id", "title")
                    .addRecords(sql, new Record().setValue("name", "aaa").setValue("count", 1),
                            new Record().setValue("name", "ccc").setValue("count", 3))
                    .getMockedConnection();
        });

        List<Tuple2<String, Integer>> tuples = from(Document.class, User.class, Category.class).select((d, u, c) -> {
            u.getName();
            count();
        }).where((d, u, c) -> {
            cond(d.getOwnerId(), "=", u.getId());
            cond(d.getCategoryId(), "=", c.getId());
        }).groupBy((d, u, c) -> {
            u.getName();
        }).orderBy((d, u, c) -> {
            asc(u.getName());
        }).queryList_by_mapEach(rs -> {
            String name = rs.getString(1);
            int count = rs.getInt(2);
            return new Tuple2<>(name, count);
        });
        assertEquals(Arrays.asList(new Tuple2<>("aaa", 1), new Tuple2<>("ccc", 3)), tuples);
    }

}
