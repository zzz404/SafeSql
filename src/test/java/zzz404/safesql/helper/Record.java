package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.mockito.Mockito;

public class Record {
    private Object[] values;

    public static Record[] singleColumn(Object... values) {
        Record[] records = new Record[values.length];
        for (int i = 0; i < values.length; i++) {
            records[i] = new Record().setValues(values[i]);
        }
        return records;
    }

    public Record setValues(Object... values) {
        this.values = values;
        return this;
    }

    private int objectToInt(Object o) {
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        }
        else {
            return Integer.parseInt((String) o);
        }
    }

    public String getString(int index) {
        return get(index).toString();
    }

    public int getInt(int index) {
        return objectToInt(get(index));
    }

    private Object get(int index) {
        return values[index - 1];
    }

    public static void bindData(ResultSet rs, Record[] recs) throws SQLException {
        Record[] records = (recs != null) ? recs : new Record[0];
        Mockito.reset(rs);
        int[] row = new int[] { -1 };
        when(rs.next()).thenAnswer(i -> {
            row[0]++;
            return (row[0] < records.length);
        });
        when(rs.absolute(anyInt())).then(info -> {
            int position = (Integer) info.getArguments()[0];
            row[0] = position - 1;
            return position > 0 && position <= records.length;
        });
        when(rs.getInt(anyInt())).thenAnswer(info -> {
            int row0 = row[0];
            int index = (Integer) info.getArguments()[0];
            if (row0 >= 0 && row0 < records.length) {
                return records[row0].getInt(index);
            }
            else {
                throw new RuntimeException(new SQLException());
            }
        });
        when(rs.getString(anyInt())).thenAnswer(info -> {
            int row0 = row[0];
            int index = (Integer) info.getArguments()[0];
            if (row0 >= 0 && row0 < records.length) {
                return records[row0].getString(index);
            }
            else {
                throw new RuntimeException(new SQLException());
            }
        });
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(0);
    }

}
