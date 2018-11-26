package zzz404.safesql.querier;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.ConnFactoryBackDoor;
import zzz404.safesql.ConnectionFactory;
import zzz404.safesql.querier.OneEntityQuerier;
import zzz404.safesql.querier.StaticSqlQuerier;

public class TestSql {
    @BeforeEach
    void beforeEach() {
        ConnectionFactory.create("", () -> null);
    }

    @AfterEach
    void afterEach() {
        ConnFactoryBackDoor.removeAllFactories();
    }
    
    @Test
    void test_sql_useDefault() {
        StaticSqlQuerier querier = sql("hi");
        assertEquals("", ConnFactoryBackDoor.getName(querier.connFactory));
    }

    @Test
    void test_from_useDefault() {
        OneEntityQuerier<?> querier = from(Object.class);
        assertEquals("", ConnFactoryBackDoor.getName(querier.connFactory));
    }

}
