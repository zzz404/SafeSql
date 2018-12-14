package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.text.Document;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import zzz404.safesql.ConnectionManager;
import zzz404.safesql.DbSourceBackDoor;
import zzz404.safesql.Entity;
import zzz404.safesql.Field;
import zzz404.safesql.helper.Category;
import zzz404.safesql.helper.FakeSchemaBase;
import zzz404.safesql.helper.User;

public class TestDbSourceImpl {

    @AfterEach
    void tearDown() {
        DbSourceBackDoor.removeAllFactories();
    }

    @Test
    void test_withConnection_useConnectionProvider() throws SQLException {
        Connection conn = mock(Connection.class);
        DbSourceImpl ds = new DbSourceImpl("");
        ds.useConnectionPrivider(() -> conn);
        ds.withConnection(enConn -> {
            enConn.isClosed();
            Connection conn1 = QuietConnectionBackDoor.getConnection(enConn);
            assertTrue(conn == conn1);
            return null;
        });
        verify(conn, times(1)).isClosed();
        verify(conn, times(1)).close();
    }

    @Test
    void test_withConnection_useConnectionManager() throws SQLException {
        Connection conn = mock(Connection.class);
        DbSourceImpl ds = new DbSourceImpl("");
        ds.useConnectionManager(new ConnectionManager() {
            @Override
            public <T> T underConnection(Function<Connection, T> func) throws SQLException {
                conn.isClosed();
                return func.apply(conn);
            }
        });
        ds.withConnection(enConn -> {
            Connection conn1 = QuietConnectionBackDoor.getConnection(enConn);
            assertTrue(conn == conn1);
            return null;
        });
        verify(conn, times(1)).isClosed();
        verify(conn, times(0)).close();
    }

    @Test
    void test_getRealTableName_noSnake() throws SQLException {
        DbSourceImpl ds = new DbSourceImpl("zzz");
        assertEquals("AaaBbb", ds.getRealTableName("AaaBbb"));

        ds.withTablePrefix(true);
        assertEquals("zzzAaaBbb", ds.getRealTableName("AaaBbb"));
    }

    @ParameterizedTest
    @CsvSource({ "zzz, DocUser", "DoC_uSEr, doc_user", "DoC_uSEr|dOcuseR, DocUser" })
    void test_getRealTableName_snake_noPrefix(String realTables, String expectedTable) throws SQLException {
        DbSourceImpl ds = new DbSourceImpl("zzz");
        ds.snakeFormCompatable(true);
        ds.useConnectionPrivider(() -> buildConnection(StringUtils.split(realTables, '|')));
        assertEquals(expectedTable, ds.getRealTableName("DocUser"));
    }

    private QuietConnection buildConnection(String... tableNames) throws SQLException {
        Connection conn = new FakeSchemaBase().addTables(tableNames).getMockedConnection();
        return new QuietConnection(conn);
    }

    @ParameterizedTest
    @CsvSource({ "Doc_User, DocUser", "zzzDoC_uSEr, zzzdoc_user", "zzzDoC_uSEr|dOcuseR, zzzdoc_user",
            "zzzDoC_uSEr|zzzdOcuseR, zzzDocUser" })
    void test_getRealTableName_snake_prefix(String realTables, String expectedTable) throws SQLException {
        DbSourceImpl ds = new DbSourceImpl("zzz");
        ds.withTablePrefix(true).snakeFormCompatable(true);
        ds.useConnectionPrivider(() -> buildConnection(StringUtils.split(realTables, '|')));
        assertEquals(expectedTable, ds.getRealTableName("DocUser"));
    }

    @Test
    void test_getSchema_cache() {
        DbSourceImpl ds = new DbSourceImpl("zzz");
        ds.useConnectionPrivider(() -> buildConnection());

        TableSchema schema1 = ds.getSchema_for_snakeCompatable("aaa");
        TableSchema schema2 = ds.getSchema_for_snakeCompatable("bbb");

        assertNotEquals(schema1, schema2);

        TableSchema schema3 = ds.getSchema_for_snakeCompatable("aaa");
        assertEquals(schema1, schema3);
    }

    @Test
    void test_revise_snake() throws SQLException {
        Connection conn = new FakeSchemaBase().addTable("Document", "doc_title", "ownerId")
                .addTable("User", "full_name").getMockedConnection();

        DbSourceImpl ds = new DbSourceImpl("");
        ds.snakeFormCompatable(true).useConnectionPrivider(() -> new QuietConnection(conn));

        List<Entity<?>> entities = Arrays.asList(createEntity(1, Document.class, "docTitle", "ownerId"),
                createEntity(2, User.class, "firstName", "fullName"), createEntity(3, Category.class, "parentId"));

        ds.revise(entities);

        List<String> columns = entities.get(0).getFields().stream().map(Field::getPrefixedRealColumnName)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("t1.doc_title", "t1.ownerId"), columns);

        columns = entities.get(1).getFields().stream().map(Field::getPrefixedRealColumnName)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("t2.firstName", "t2.full_name"), columns);

        columns = entities.get(2).getFields().stream().map(Field::getPrefixedRealColumnName)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("t3.parentId"), columns);
    }

    @Test
    void test_revise_noSnake() throws SQLException {
        Connection conn = new FakeSchemaBase().addTable("Document", "doc_title").getMockedConnection();
        DbSourceImpl ds = new DbSourceImpl("");
        ds.snakeFormCompatable(false).useConnectionPrivider(() -> new QuietConnection(conn));

        List<Entity<?>> entities = Arrays.asList(createEntity(1, Document.class, "docTitle"));

        ds.revise(entities);

        List<String> columns = entities.get(0).getFields().stream().map(Field::getPrefixedRealColumnName)
                .collect(Collectors.toList());
        assertEquals(Arrays.asList("t1.docTitle"), columns);
    }

    private <T> Entity<T> createEntity(int index, Class<T> clazz, String... columns) {
        Entity<T> entity = new Entity<>(index, clazz);
        for (String column : columns) {
            new Field(entity, column);
        }
        return entity;
    }

}
