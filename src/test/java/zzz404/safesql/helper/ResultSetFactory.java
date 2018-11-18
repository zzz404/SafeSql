package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.util.CommonUtils;

public class ResultSetFactory {

    protected int row = -1;
    protected Record[] records;

    public static ResultSetFactory singleColumn(int... values) {
        return new ResultSetFactory().reset(values);
    }

    public static ResultSetFactory singleColumn(String... values) {
        return new ResultSetFactory().reset(values);
    }

    public ResultSetFactory reset(int... values) {
        this.row = -1;
        records = Record.singleColumn(values);
        return this;
    }

    public ResultSetFactory reset(String... values) {
        this.row = -1;
        records = Record.singleColumn((Object[]) values);
        return this;
    }

    public QuietResultSet createQuiet() {
        QuietResultSet rs = mock(QuietResultSet.class);
        try {
            setup(rs);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
        return rs;
    }

    public ResultSet createNormal() {
        ResultSet rs = mock(ResultSet.class);
        try {
            setup(rs);
        }
        catch (SQLException e) {
            throw CommonUtils.wrapToRuntime(e);
        }
        return rs;
    }

    private void setup(ResultSet rs) throws SQLException {
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
    }
}
