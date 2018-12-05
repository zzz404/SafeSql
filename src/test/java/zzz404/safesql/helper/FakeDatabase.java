package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

import zzz404.safesql.util.NoisyRunnable;

public class FakeDatabase {
    public Connection conn = mock(Connection.class);
    public PreparedStatement pstmt = mock(PreparedStatement.class);
    public Statement stmt = mock(Statement.class);
    public ResultSet rs = mock(ResultSet.class);

    protected int row = -1;
    protected Queue<Record[]> data = new LinkedList<>();

    public FakeDatabase() {
        NoisyRunnable.runQuietly(() -> {
            when(conn.prepareStatement(anyString())).thenReturn(pstmt);
            when(conn.prepareStatement(anyString(), anyInt(), anyInt())).thenReturn(pstmt);
            when(pstmt.executeQuery()).then(info -> {
                rs = mock(ResultSet.class);
                Record[] records = data.poll();
                Record.bindData(rs, records);
                return rs;
            });

            when(conn.createStatement()).thenReturn(stmt);
            when(conn.createStatement(anyInt(), anyInt())).thenReturn(stmt);
            when(stmt.executeQuery(anyString())).then(info -> {
                rs = mock(ResultSet.class);
                Record[] records = data.poll();
                Record.bindData(rs, records);
                return rs;
            });
        });
    }

    public void pushData(Object... values) {
        Record[] records = Record.singleColumn(values);
        data.offer(records);
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
