package zzz404.safesql.helper;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.mockito.stubbing.Answer;

import zzz404.safesql.util.NoisySupplier;

public class FakeDatabase {
    private static final Connection defaultConnection = NoisySupplier.getQuietly(() -> {
        return new FakeDatabase().addTableColumns("Document", "id", "ownerId", "title")
                .addTableColumns("User", "id", "name").addTableColumns("Category", "id", "name").getMockedConnection();
    });

    public static Connection getDefaultconnection() {
        return FakeDatabase.defaultConnection;
    }

    private Connection conn = null;
    private Statement stmt = null;

    protected Map<String, PreparedStatement> pstmtMap = new HashMap<>();
    protected Map<String, ResultSet> rsMap = new HashMap<>();

    public FakeDatabase addTables(String... tables) throws SQLException {
        for (String table : tables) {
            addTableColumns(table);
        }
        return this;
    }

    public FakeDatabase addTableColumns(String table, String... columnLabels) throws SQLException {
        ResultSet rs = mock(ResultSet.class);
        ResultSetMetaData meta = mock(ResultSetMetaData.class);
        when(rs.getMetaData()).thenReturn(meta);
        when(meta.getColumnCount()).thenReturn(columnLabels.length);
        if (columnLabels.length > 0) {
            when(meta.getColumnLabel(anyInt())).then(info -> {
                int index = (Integer) info.getArgument(0);
                return columnLabels[index - 1];
            });
        }
        rsMap.put("SELECT * FROM " + table, rs);
        return this;
    }

    public FakeDatabase addRecords(String sql, Record... records) throws SQLException {
        ResultSet rs = new RecordsResultBuilder(records).getResultSet();
        rsMap.put(sql, rs);
        return this;
    }

    public FakeDatabase addSingleColumnValues(String sql, String columnLabel, int... values) throws SQLException {
        Record[] records = Record.singleColumn(columnLabel, values);
        addRecords(sql, records);
        return this;
    }

    public FakeDatabase addSingleColumnValues(String sql, String columnLabel, String... values) throws SQLException {
        Record[] records = Record.singleColumn(columnLabel, values);
        addRecords(sql, records);
        return this;
    }

    public Connection getMockedConnection() throws SQLException {
        if (conn == null) {
            conn = mock(Connection.class);
            this.stmt = mock(Statement.class);
            when(conn.createStatement()).thenReturn(stmt);
            when(conn.createStatement(anyInt(), anyInt())).thenReturn(stmt);
            when(stmt.executeQuery(anyString())).then(info -> {
                String sql = info.getArgument(0);
                return rsMap.get(sql);
            });

            Answer<?> answer = info -> {
                String sql = info.getArgument(0);
                PreparedStatement pstmt = pstmtMap.get(sql);
                if (pstmt == null) {
                    pstmt = mock(PreparedStatement.class);
                    ResultSet rs = rsMap.get(sql);
                    when(pstmt.executeQuery()).thenReturn(rs);
                    pstmtMap.put(sql, pstmt);
                }
                return pstmt;
            };
            when(conn.prepareStatement(anyString())).then(answer);
            when(conn.prepareStatement(anyString(), anyInt(), anyInt())).then(answer);
        }
        return conn;
    }

}
