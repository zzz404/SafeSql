package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

public class TestTableSchema {
    @Test
    void test_createByQuery_noSnakeCompatable() throws SQLException {
        Statement stmt = createFakeStatement("DocUser");

        assertThrows(RuntimeException.class,
                () -> TableSchema.createByQuery("UserDoc", false, new QuietStatement(stmt)));
        TableSchema tableSchema = TableSchema.createByQuery("DocUser", false, new QuietStatement(stmt));
        assertEquals("DocUser", tableSchema.getVirtualTableName());
        assertEquals("DocUser", tableSchema.getRealTableName());
        assertTrue(tableSchema.realColumnNames.isEmpty());
    }

    private Statement createFakeStatement(String tableName, String... columnNames) throws SQLException {
        Statement stmt = mock(Statement.class);
        ResultSet rs = mock(ResultSet.class);
        when(stmt.executeQuery(anyString())).thenAnswer(info -> {
            String sql = info.getArgument(0);
            if (sql.endsWith(tableName)) {
                return rs;
            }
            else {
                throw new SQLException();
            }
        });
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(columnNames.length);
        when(meta.getColumnName(anyInt())).thenAnswer(info -> {
            int index = info.getArgument(0);
            return columnNames[index - 1];
        });
        return stmt;
    }
}
