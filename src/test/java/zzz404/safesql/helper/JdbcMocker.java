package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.sql.QuietResultSetMetaData;

public class JdbcMocker {

    public static ResultSetMetaData mockMetaData(String... columnNames) throws SQLException {
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(meta.getColumnCount()).thenReturn(columnNames.length);
        when(meta.getColumnName(anyInt())).thenAnswer(info -> {
            int index = info.getArgument(0);
            return columnNames[index - 1];
        });
        return meta;
    }

    public static QuietResultSetMetaData mockQuietMetaData(String... columnNames) throws SQLException {
        ResultSetMetaData meta = mockMetaData(columnNames);
        return new QuietResultSetMetaData(meta);
    }

    public static ResultSet mockResultSet(ResultSetMetaData meta, Record... recs) throws SQLException {
        ResultSet rs = new RecordsResultBuilder(recs).getResultSet();
        if (meta != null) {
            when(rs.getMetaData()).thenReturn(meta);
        }
        return rs;
    }

    public static ResultSet mockResultSet(Record... recs) throws SQLException {
        return mockResultSet(null, recs);
    }

    public static ResultSet mockQuietResultSet(ResultSetMetaData meta, Record... recs) throws SQLException {
        ResultSet rs = mockResultSet(meta, recs);
        return new QuietResultSet(rs);
    }

}
