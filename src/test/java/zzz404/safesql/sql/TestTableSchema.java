package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import zzz404.safesql.Entity;
import zzz404.safesql.Field;
import zzz404.safesql.FieldBackDoor;
import zzz404.safesql.util.CommonUtils;

public class TestTableSchema {
    @Test
    void test_createByQuery_noSnakeCompatable() throws SQLException {
        Statement stmt = new StatementBuilder().addTable("DocUser").build();

        assertThrows(RuntimeException.class,
                () -> TableSchema.createByQuery("UserDoc", false, new QuietStatement(stmt)));
        TableSchema tableSchema = TableSchema.createByQuery("DocUser", false, new QuietStatement(stmt));
        assertEquals("DocUser", tableSchema.getVirtualTableName());
        assertEquals("DocUser", tableSchema.getRealTableName());
        assertTrue(tableSchema.prop_real_map.isEmpty());
    }

    @Test
    void test_createByQuery_snakeCompatable() throws SQLException {
        Statement stmt = new StatementBuilder().addTable("DOC_USER").build();

        assertThrows(RuntimeException.class,
                () -> TableSchema.createByQuery("UserDoc", true, new QuietStatement(stmt)));

        TableSchema tableSchema = TableSchema.createByQuery("DocUser", true, new QuietStatement(stmt));
        assertEquals("DocUser", tableSchema.getVirtualTableName());
        assertEquals("doc_user", tableSchema.getRealTableName());
        assertTrue(tableSchema.prop_real_map.isEmpty());
    }

    @Test
    void test_createByQuery_snakeCompatable_chooseCamel() throws SQLException {
        Statement stmt = new StatementBuilder().addTable("docUser").addTable("DOC_USER").build();

        TableSchema tableSchema = TableSchema.createByQuery("DocUser", true, new QuietStatement(stmt));
        assertEquals("DocUser", tableSchema.getVirtualTableName());
        assertEquals("DocUser", tableSchema.getRealTableName());
    }

    @Test
    void test_createByQuery_snakeCompatable_columnConflict() throws SQLException {
        Statement stmt = new StatementBuilder().addTable("DOC_USER", "userId", "user_id").build();

        assertThrows(TableSchemeException.class,
                () -> TableSchema.createByQuery("DocUser", true, new QuietStatement(stmt)));
    }

    @Test
    void test_realColumnNames() throws SQLException {
        Statement stmt = new StatementBuilder().addTable("DocUser", "userId", "docTitle", "user_ID", "DOC_title")
                .build();
        Set<String> columns = CommonUtils.newSet("userid", "doctitle", "user_id", "doc_title");

        TableSchema tableSchema = TableSchema.createByQuery("DocUser", false, new QuietStatement(stmt));
        assertEquals(columns, tableSchema.prop_real_map.keySet());

        tableSchema = TableSchema.createByQuery("DocUser", true, new QuietStatement(stmt));
        assertEquals(columns, tableSchema.prop_real_map.keySet());
    }

    @Test
    void test__revise_for_snakeFormCompatable__sameColumnName() throws SQLException {
        Statement stmt = new StatementBuilder().addTable("DocUser", "userId", "doc_title").build();
        TableSchema tableSchema = TableSchema.createByQuery("DocUser", true, new QuietStatement(stmt));

        Entity<?> entity = mock(Entity.class);
        Field field = new Field(entity, "userId");
        tableSchema.revise_for_snakeFormCompatable(field);

        assertEquals("userId", FieldBackDoor.getRealColumnName(field));
    }

    @Test
    void test__revise_for_snakeFormCompatable__snakedColumnName() throws SQLException {
        Statement stmt = new StatementBuilder().addTable("DocUser", "userId", "doc_title").build();
        TableSchema tableSchema = TableSchema.createByQuery("DocUser", true, new QuietStatement(stmt));

        Entity<?> entity = mock(Entity.class);
        Field field = new Field(entity, "docTitle");
        tableSchema.revise_for_snakeFormCompatable(field);

        assertEquals("doc_title", FieldBackDoor.getRealColumnName(field));
    }

    @Test
    void test__revise_for_snakeFormCompatable__noColumnName() throws SQLException {
        Statement stmt = new StatementBuilder().addTable("DocUser", "userId", "doc_title").build();
        TableSchema tableSchema = TableSchema.createByQuery("DocUser", true, new QuietStatement(stmt));

        Entity<?> entity = mock(Entity.class);
        Field field = new Field(entity, "docId");
        tableSchema.revise_for_snakeFormCompatable(field);

        assertEquals("docId", FieldBackDoor.getRealColumnName(field));
    }

    public static class StatementBuilder {
        private Map<String, ResultSet> map = new HashMap<>();

        public StatementBuilder addTable(String tableName, String... columnNames) throws SQLException {
            ResultSet rs = mock(ResultSet.class);
            ResultSetMetaData meta = mock(ResultSetMetaData.class);
            when(rs.getMetaData()).thenReturn(meta);
            when(meta.getColumnCount()).thenReturn(columnNames.length);
            when(meta.getColumnName(anyInt())).thenAnswer(info -> {
                int index = info.getArgument(0);
                return columnNames[index - 1];
            });
            map.put(tableName.toLowerCase(), rs);
            return this;
        }

        public Statement build() throws SQLException {
            Statement stmt = mock(Statement.class);
            when(stmt.executeQuery(anyString())).thenAnswer(info -> {
                String sql = info.getArgument(0);
                sql = sql.toLowerCase();
                for (String tableName : map.keySet()) {
                    if (sql.endsWith(tableName)) {
                        return map.get(tableName);
                    }
                }
                throw new SQLException();
            });
            return stmt;
        }

    }

}
