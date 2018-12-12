package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.ArrayUtils;

import zzz404.safesql.util.NoisyRunnable;

class RecordsResultBuilder {
    private Record[] recs;
    private int position = -1;
    private ResultSet rs = mock(ResultSet.class);
    private ResultSetMetaData meta = mock(ResultSetMetaData.class);

    public RecordsResultBuilder(Record... recs) {
        this.recs = recs != null ? recs : new Record[0];
        NoisyRunnable.runQuietly(() -> simulate());
    }

    private void simulate() throws SQLException {
        simulateMetaData();
        simulatePosition();
        simulateGet();
    }

    private void simulateMetaData() throws SQLException {
        when(rs.getMetaData()).thenReturn(meta);
        if (ArrayUtils.isEmpty(recs)) {
            when(meta.getColumnCount()).thenReturn(0);
        }
        else {
            when(meta.getColumnCount()).thenReturn(recs[0].getColumnCount());
            when(meta.getColumnName(anyInt())).then(info -> {
                int index = (Integer) info.getArgument(0);
                return recs[0].getColumnName(index);
            });
        }
    }

    private void simulateGet() throws SQLException {
        when(rs.getInt(anyInt())).thenAnswer(info -> {
            int index = (Integer) info.getArgument(0);
            return getCurrentRecord().getInt(index);
        });
        when(rs.getInt(anyString())).thenAnswer(info -> {
            String columnName = (String) info.getArgument(0);
            return getCurrentRecord().getInt(columnName);
        });
        when(rs.getString(anyInt())).thenAnswer(info -> {
            int index = (Integer) info.getArgument(0);
            return getCurrentRecord().getString(index);
        });
        when(rs.getString(anyString())).thenAnswer(info -> {
            String columnName = (String) info.getArgument(0);
            return getCurrentRecord().getString(columnName);
        });
    }

    private void simulatePosition() throws SQLException {
        when(rs.next()).thenAnswer(i -> {
            position++;
            return position < recs.length;
        });
        when(rs.absolute(anyInt())).then(info -> {
            position = (Integer) info.getArgument(0) - 1;
            return isValidRange();
        });
    }

    private Record getCurrentRecord() {
        if (isValidRange()) {
            return recs[position];
        }
        else {
            throw new RuntimeException(new SQLException());
        }
    }

    private boolean isValidRange() {
        return position >= 0 && position < recs.length;
    }

    public ResultSet getResultSet() {
        return rs;
    }

}