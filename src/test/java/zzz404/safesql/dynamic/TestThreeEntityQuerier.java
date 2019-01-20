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
import zzz404.safesql.helper.Category;
import zzz404.safesql.helper.Document;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.Record;
import zzz404.safesql.helper.User;
import zzz404.safesql.util.Tuple3;

class TestThreeEntityQuerier {

    private String sql = "SELECT t1.title, t2.name, t3.title FROM Document t1, User t2, Category t3"
            + " WHERE t1.ownerId = t2.id AND t1.categoryId = t3.id AND t3.id = ? ORDER BY t1.id ASC";
    private String sql_for_count = "SELECT COUNT(*) FROM Document t1, User t2, Category t3"
            + " WHERE t1.ownerId = t2.id AND t1.categoryId = t3.id AND t3.id = ?";
    private FakeDatabase fakeDb;

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
        fakeDb = null;
    }

    private void registerDefaultConnectionProvider() throws SQLException {
        fakeDb = new FakeDatabase().addTableColumns("Document", "id", "title", "ownerId", "categoryId")
                .addTableColumns("User", "id", "name").addTableColumns("Category", "id", "title")
                .addSingleColumnValues(sql_for_count, "", 4).addRecords(sql,
                        new Record().setValue("Document", "title", "title1").setValue("User", "name", "name1")
                                .setValue("Category", "title", "ct1"),
                        new Record().setValue("Document", "title", "title2").setValue("User", "name", "name2")
                                .setValue("Category", "title", "ct2"),
                        new Record().setValue("Document", "title", "title3").setValue("User", "name", "name3")
                                .setValue("Category", "title", "ct3"),
                        new Record().setValue("Document", "title", "title4").setValue("User", "name", "name4")
                                .setValue("Category", "title", "ct4"));
        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
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
    void test_queryOne() throws SQLException {
        registerDefaultConnectionProvider();

        Tuple3<Document, User, Category> duc = createQuerier().queryOne().get();
        assertEquals("title2", duc.first().getTitle());
        assertEquals("name2", duc.second().getName());
        assertEquals("ct2", duc.third().getTitle());

        fakeDb.assertParamValues(sql, 123);
    }

    @Test
    void test_queryList() throws SQLException {
        registerDefaultConnectionProvider();

        List<Tuple3<String, String, String>> tuples = createQuerier().queryList().stream().map(duc -> {
            return new Tuple3<>(duc.first().getTitle(), duc.second().getName(), duc.third().getTitle());
        }).collect(Collectors.toList());
        assertEquals(Arrays.asList(new Tuple3<>("title2", "name2", "ct2"), new Tuple3<>("title3", "name3", "ct3")),
                tuples);
    }

    @Test
    void test_queryPage() throws SQLException {
        registerDefaultConnectionProvider();

        Page<Tuple3<Document, User, Category>> page = createQuerier().queryPage();
        assertEquals(4, page.getTotalCount());
        assertEquals(Arrays.asList(new Tuple3<>("title2", "name2", "ct2"), new Tuple3<>("title3", "name3", "ct3")),
                page.getResult().stream().map(duc -> {
                    return new Tuple3<>(duc.first().getTitle(), duc.second().getName(), duc.third().getTitle());
                }).collect(Collectors.toList()));

        fakeDb.assertParamValues(sql, 123);
        fakeDb.assertParamValues(sql_for_count, 123);
    }

    @Test
    void test_queryEntityStream() throws SQLException {
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
    void test_groupBy() throws SQLException {
        String sql = "SELECT t2.name, t3.name, COUNT(*) FROM Document t1, User t2, Category t3"
                + " WHERE t1.ownerId = t2.id AND t1.categoryId = t3.id"
                + " GROUP BY t2.name, t3.name ORDER BY t2.name ASC, t3.name DESC";
        String sql_count = "SELECT COUNT(*) FROM Document t1, User t2, Category t3"
                + " WHERE t1.ownerId = t2.id AND t1.categoryId = t3.id"
                + " GROUP BY t2.name, t3.name";
        FakeDatabase fakeDb = new FakeDatabase().addTables("Document", "User", "Category");
        fakeDb.addSingleColumnValues(sql_count, "", 4);
        fakeDb.addRecords(sql,
                new Record().setValue("User", "name", "o1").setValue("Category", "name", "c1").setValue("", "count",
                        11),
                new Record().setValue("User", "name", "o2").setValue("Category", "name", "c2").setValue("", "count",
                        22),
                new Record().setValue("User", "name", "o3").setValue("Category", "name", "c3").setValue("", "count",
                        33),
                new Record().setValue("User", "name", "o4").setValue("Category", "name", "c4").setValue("", "count",
                        44));
        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
        });

        ThreeEntityQuerier<Document, User, Category> querier = from(Document.class, User.class, Category.class)
                .select((d, u, c) -> {
                    u.getName();
                    c.getName();
                    count();
                }).where((d, u, c) -> {
                    cond(d.getOwnerId(), "=", u.getId());
                    cond(d.getCategoryId(), "=", c.getId());
                }).groupBy((d, u, c) -> {
                    u.getName();
                    c.getName();
                }).orderBy((d, u, c) -> {
                    asc(u.getName());
                    desc(c.getName());
                }).offset(1).limit(2);

        Page<Tuple3<String, String, Integer>> page = querier.queryPage_by_mapEach(rs -> {
            return new Tuple3<>(rs.getString(1), rs.getString(2), rs.getInt(3));
        });

        assertEquals(4, page.getTotalCount());
        assertEquals(Arrays.asList(new Tuple3<>("o2", "c2", 22), new Tuple3<>("o3", "c3", 33)), page.getResult());
    }

}
