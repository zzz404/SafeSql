package zzz404.safesql.sql;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.Test;

public class TestEnhancedConnection {

    @Test
    void test_createStatement_multipleCalled_useCache() throws SQLException {
        Connection conn = createMockedConnection();
        EnhancedConnection enConn = new EnhancedConnection(conn);
        QuietStatement stmt = enConn.createStatement();
        QuietStatement stmt2 = enConn.createStatement();
        assertTrue(stmt == stmt2);
        enConn.close();
    }

    @Test
    void test_createStatement_withResultSetParemeters() throws SQLException {
        Connection conn = createMockedConnection();
        EnhancedConnection enConn = new EnhancedConnection(conn);

        QuietStatement stmt = enConn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        QuietStatement stmt2 = enConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        assertNotEquals(stmt, stmt2);

        QuietStatement stmt3 = enConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        assertNotEquals(stmt2, stmt3);

        QuietStatement stmt4 = enConn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        assertEquals(stmt3, stmt4);

        QuietStatement stmt5 = enConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        assertEquals(stmt4, stmt5);

        QuietStatement stmt6 = enConn.createStatement();
        assertEquals(stmt5, stmt6);

        enConn.close();
    }

    @Test
    void test_createStatement_statementClosedBeforeChanged() throws SQLException {
        Connection conn = createMockedConnection();
        EnhancedConnection enConn = new EnhancedConnection(conn);
        QuietStatement stmt = enConn.createStatement();
        enConn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
        verify(stmt.stmt, times(1)).close();

        enConn.close();
    }

    @Test
    void test_prepareStatement_multipleCalled_useCache() throws SQLException {
        Connection conn = createMockedConnection();
        EnhancedConnection enConn = new EnhancedConnection(conn);
        QuietPreparedStatement pstmt = enConn.prepareStatement("");
        QuietPreparedStatement pstmt2 = enConn.prepareStatement("");
        assertTrue(pstmt == pstmt2);
        enConn.close();
    }

    @Test
    void test_prepareStatement_differentSql() throws SQLException {
        Connection conn = createMockedConnection();
        EnhancedConnection enConn = new EnhancedConnection(conn);
        QuietPreparedStatement pstmt = enConn.prepareStatement("");
        QuietPreparedStatement pstmt2 = enConn.prepareStatement("zzz");

        assertNotEquals(pstmt, pstmt2);

        enConn.close();
    }

    @Test
    void test_prepareStatement_withResultSetParemeters() throws SQLException {
        Connection conn = createMockedConnection();
        EnhancedConnection enConn = new EnhancedConnection(conn);

        QuietPreparedStatement pstmt = enConn.prepareStatement("", ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);

        QuietPreparedStatement pstmt2 = enConn.prepareStatement("", ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        assertNotEquals(pstmt, pstmt2);

        QuietPreparedStatement pstmt3 = enConn.prepareStatement("", ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        assertNotEquals(pstmt2, pstmt3);

        QuietPreparedStatement pstmt4 = enConn.prepareStatement("", ResultSet.TYPE_SCROLL_SENSITIVE,
                ResultSet.CONCUR_UPDATABLE);
        assertEquals(pstmt3, pstmt4);

        QuietPreparedStatement pstmt5 = enConn.prepareStatement("", ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        assertEquals(pstmt4, pstmt5);

        QuietPreparedStatement pstmt6 = enConn.prepareStatement("");
        assertEquals(pstmt5, pstmt6);

        enConn.close();
    }

    @Test
    void test_prepareStatement_statementClosedBeforeChanged() throws SQLException {
        Connection conn = createMockedConnection();
        EnhancedConnection enConn = new EnhancedConnection(conn);
        QuietPreparedStatement pstmt = enConn.prepareStatement("");
        enConn.prepareStatement("", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        
        verify(pstmt.pstmt, times(1)).close();

        enConn.close();
    }

    @Test
    void test_closeStatement_noExceptiomThrown() throws SQLException {
        @SuppressWarnings("resource")
        EnhancedConnection enConn = new EnhancedConnection(null);
        enConn.closeStatement(null);
    }

    @Test
    void test_close() throws SQLException {
        Connection conn = createMockedConnection();
        EnhancedConnection enConn = new EnhancedConnection(conn);
        QuietStatement stmt = enConn.createStatement();
        QuietPreparedStatement pstmt1 = enConn.prepareStatement("aaa");
        QuietPreparedStatement pstmt2 = enConn.prepareStatement("bbb");

        enConn.close();

        verify(stmt.stmt, times(1)).close();
        verify(pstmt1.pstmt, times(1)).close();
        verify(pstmt2.pstmt, times(1)).close();
    }

    private Connection createMockedConnection() throws SQLException {
        Connection conn = mock(Connection.class);
        when(conn.createStatement()).then(info -> {
            return createMockedStatement();
        });
        when(conn.createStatement(anyInt(), anyInt())).then(info -> {
            int resultSetType = info.getArgument(0);
            int resultSetConcurrency = info.getArgument(1);
            return createMockedStatement(resultSetType, resultSetConcurrency);
        });
        when(conn.prepareStatement(anyString())).thenAnswer(info -> {
            return createMockedPreparedStatement();
        });
        when(conn.prepareStatement(anyString(), anyInt(), anyInt())).then(info -> {
            int resultSetType = info.getArgument(1);
            int resultSetConcurrency = info.getArgument(2);
            return createMockedPreparedStatement(resultSetType, resultSetConcurrency);
        });
        return conn;
    }

    private Statement createMockedStatement() throws SQLException {
        return createMockedStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    private Statement createMockedStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement stmt = mock(Statement.class);
        when(stmt.getResultSetType()).thenReturn(resultSetType);
        when(stmt.getResultSetConcurrency()).thenReturn(resultSetConcurrency);
        return stmt;
    }

    private PreparedStatement createMockedPreparedStatement() throws SQLException {
        return createMockedPreparedStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    private PreparedStatement createMockedPreparedStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(pstmt.getResultSetType()).thenReturn(resultSetType);
        when(pstmt.getResultSetConcurrency()).thenReturn(resultSetConcurrency);
        return pstmt;
    }

}
