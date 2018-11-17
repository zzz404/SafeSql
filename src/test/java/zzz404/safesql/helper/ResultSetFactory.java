package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import zzz404.safesql.sql.QuietResultSet;

public class ResultSetFactory {

    protected int row = -1;
    protected Record[] records;

    public static ResultSetFactory singleColumn(int... values) {
        ResultSetFactory f = new ResultSetFactory();
        f.records = new Record[values.length];
        for (int i = 0; i < values.length; i++) {
            f.records[i] = Record.singleColumn(values[i]);
        }
        return f;
    }

    public static ResultSetFactory singleColumn(String... values) {
        ResultSetFactory f = new ResultSetFactory();
        f.records = new Record[values.length];
        for (int i = 0; i < values.length; i++) {
            f.records[i] = Record.singleColumn(values[i]);
        }
        return f;
    }

    public QuietResultSet createMocked() {
        QuietResultSet rs = mock(QuietResultSet.class);
        when(rs.next()).thenAnswer(i -> {
            row++;
            return (row < records.length);
        });
        when(rs.absolute(anyInt())).then(info -> {
            int position = (Integer) info.getArguments()[0];
            row = position - 1;
            return position > 0 && position <= records.length;
        });
        when(rs.getInt(anyInt())).thenAnswer(info -> {
            int index = (Integer) info.getArguments()[0];
            if (row >= 0 && row < records.length) {
                return records[row].getInt(index);
            }
            else {
                throw new RuntimeException(new SQLException());
            }
        });
        when(rs.getString(anyInt())).thenAnswer(info -> {
            int index = (Integer) info.getArguments()[0];
            if (row >= 0 && row < records.length) {
                return records[row].getString(index);
            }
            else {
                throw new RuntimeException(new SQLException());
            }
        });
        return rs;
    }

}
