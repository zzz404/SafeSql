package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;

public class TestSql {
    @BeforeEach
    void beforeEach() {
        ConnectionFactory.create("", () -> null);
    }

    @AfterEach
    void afterEach() {
        ConnectionFactory.map.clear();
    }

    @Test
    void test_sql_useDefault() {
        StaticSqlQuerier querier = sql("hi");
        assertEquals("", querier.getConnectionFactory().name);
    }

    @Test
    void test_from_useDefault() {
        OneEntityQuerier<?> querier = from(Object.class);
        assertEquals("", querier.getConnectionFactory().name);
    }

    @Test
    void test_useDefault() {
        QuerierFactory factory = use();
        assertEquals("", factory.connFactory.name);
    }
}
