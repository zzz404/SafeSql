package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class FakeSchemaBase {

    protected Map<String, ResultSet> tableMap = new HashMap<>();

    public FakeSchemaBase addTables(String... tableNames) throws SQLException {
        for (String tableName : tableNames) {
            addTable(tableName);
        }
        return this;
    }

    public FakeSchemaBase addTable(String tableName, String... columnNames) throws SQLException {
        ResultSetMetaData metaData = mockMetaData(columnNames);
        ResultSet rs = mock(ResultSet.class);
        when(rs.getMetaData()).thenReturn(metaData);
        tableMap.put(tableName.toLowerCase(), rs);
        return this;
    }

    public static ResultSetMetaData mockMetaData(String... columnNames) throws SQLException {
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(metaData.getColumnCount()).thenReturn(columnNames.length);
        when(metaData.getColumnName(anyInt())).thenAnswer(info -> {
            int index = info.getArgument(0);
            return columnNames[index - 1];
        });
        return metaData;
    }

    public Connection getMockedConnection() throws SQLException {
        Connection conn = mock(Connection.class);
        Statement stmt = mock(Statement.class);

        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenAnswer(info -> {
            String sql = info.getArgument(0);
            sql = sql.toLowerCase();
            for (String tableName : tableMap.keySet()) {
                if (sql.endsWith(" " + tableName)) {
                    return tableMap.get(tableName);
                }
            }
            throw new SQLException();
        });
        return conn;
    }

}
