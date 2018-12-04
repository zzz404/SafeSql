package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.querier.QuerierBackDoor;

public class TestSql {

    @BeforeEach
    void beforeEach() {
        //        fakeDb = new FakeDatabase();
    }

    @AfterEach
    void afterEach() {
        ConnFactoryBackDoor.removeAllFactories();
    }

    @Test
    void test_use() {
        assertThrows(RuntimeException.class, () -> use("aaa"));

        DbSource.create("bbb");
        assertThrows(RuntimeException.class, () -> use("aaa"));

        DbSource.create("aaa");
        QuerierFactory qf = use("aaa");
        assertEquals("aaa", qf.dbSource.name);
    }

    @Test
    void test_useDefault() {
        assertThrows(RuntimeException.class, () -> use());

        DbSource.create();
        QuerierFactory qf = use();
        assertEquals("", qf.dbSource.name);
    }

    @Test
    void test_sql_useDefault() {
        assertThrows(RuntimeException.class, () -> sql("hi"));

        DbSource.create("");
        assertEquals("", QuerierBackDoor.getDbSource(sql("hi")).name);
    }

    @Test
    void test_from_useDefault() {
        assertThrows(RuntimeException.class, () -> from(Object.class));

        DbSource.create("");
        assertEquals("", QuerierBackDoor.getDbSource(from(Object.class)).name);
    }

}
