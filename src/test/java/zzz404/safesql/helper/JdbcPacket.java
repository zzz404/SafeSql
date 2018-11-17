package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import zzz404.safesql.NoisyRunnable;

public class JdbcPacket {
    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    protected int row = -1;
    protected Record[] records = new Record[0];

    public JdbcPacket() {
        NoisyRunnable.runQuiet(() -> {
            conn = mock(Connection.class);
            pstmt = mock(PreparedStatement.class);
            when(conn.prepareStatement(anyString())).thenReturn(pstmt);
            when(conn.prepareStatement(anyString(), anyInt(), anyInt()))
                    .thenReturn(pstmt);
            rs = mock(ResultSet.class);
            when(pstmt.executeQuery()).thenReturn(rs);
        });
    }

    public void reset() {
        row = -1;
    }

    public void bindData(int... values) {
        row = -1;
        records = Record.singleColumn(values);
        NoisyRunnable.runQuiet(() -> {
            bindData_noisy();
        });
    }

    public void bindData(String... values) {
        row = -1;
        records = Record.singleColumn(values);
        NoisyRunnable.runQuiet(() -> {
            bindData_noisy();
        });
    }

    private void bindData_noisy() throws SQLException {
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

    public Connection getConnection() {
        return conn;
    }

    public PreparedStatement getPreparedStatement() {
        return pstmt;
    }

    public ResultSet getResultSet() {
        return rs;
    }

}
