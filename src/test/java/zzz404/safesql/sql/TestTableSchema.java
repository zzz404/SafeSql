package zzz404.safesql.sql;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class TestTableSchema {

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
