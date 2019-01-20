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

class TestDynamicQuerier {

    @AfterEach
    void afterEach() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_queryPage_noCondition() throws SQLException {
        String sql = "SELECT t1.title FROM Document t1";
        String sql_for_count = "SELECT COUNT(*) FROM Document t1";

        FakeDatabase fakeDb = new FakeDatabase().addTables("Document").addSingleColumnValues(sql_for_count, "", 4)
                .addSingleColumnValues(sql, "title", "aaa", "bbb", "ccc", "ddd");
        DbSource.create().useConnectionPrivider(() -> {
            return fakeDb.getMockedConnection();
        });

        OneEntityQuerier<Document> q = from(Document.class).select(d -> {
            d.getTitle();
        }).offset(1).limit(2);

        Page<Document> page = q.queryPage();
        assertEquals(4, page.getTotalCount());

        List<String> titles = page.getResult().stream().map(Document::getTitle).collect(Collectors.toList());
        assertEquals(Arrays.asList("bbb", "ccc"), titles);
    }

}
