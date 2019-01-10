package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

public class FakeDatabase {
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private Statement stmt = null;

    protected int row = -1;
    protected Queue<ResultSet> data = new LinkedList<>();

    public FakeDatabase pushSingleColumnData(int... values) throws SQLException {
        Record[] records = Record.singleColumn(values);
        pushRecords(records);
        return this;
    }

    public FakeDatabase pushSingleColumnData(String... values) throws SQLException {
        Record[] records = Record.singleColumn(values);
        pushRecords(records);
        return this;
    }

    public FakeDatabase pushRecords(Record... records) throws SQLException {
        ResultSet rs = new RecordsResultBuilder(records).getResultSet();
        data.offer(rs);
        return this;
    }

    public FakeDatabase pushMetaData(String... columnNames) throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(columnNames.length);
        when(meta.getColumnName(anyInt())).then(info -> {
            int index = (Integer) info.getArgument(0);
            return columnNames[index - 1];
        });
        data.offer(rs);
        return this;
    }

    public ResultSet getNextResultSet() {
        return data.poll();
    }

    public Connection getMockedConnection() throws SQLException {
        if (conn == null) {
            conn = mock(Connection.class);
            this.pstmt = mock(PreparedStatement.class);
            this.stmt = mock(Statement.class);

            when(conn.prepareStatement(anyString())).thenReturn(pstmt);
            when(conn.prepareStatement(anyString(), anyInt(), anyInt())).thenReturn(pstmt);
            when(pstmt.executeQuery()).then(info -> {
                return getNextResultSet();
            });

            when(conn.createStatement()).thenReturn(stmt);
            when(conn.createStatement(anyInt(), anyInt())).thenReturn(stmt);
            when(stmt.executeQuery(anyString())).then(info -> {
                return getNextResultSet();
            });
        }
        return conn;
    }

    public Statement getMockedStatement() {
        return stmt;
    }

    public PreparedStatement getMockedPreparedStatement() {
        return pstmt;
    }

}
