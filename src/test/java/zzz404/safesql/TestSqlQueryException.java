package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.Sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.OneEntityQuerier;
import zzz404.safesql.helper.Document;

public class TestSqlQueryException {
    @Test
    void test_throw() throws SQLException {
        Connection conn = mock(Connection.class);
        PreparedStatement pstmt = mock(PreparedStatement.class);
        when(conn.prepareStatement(anyString())).thenReturn(pstmt);
        when(pstmt.executeQuery()).thenThrow(new SQLException("zzz"));
        DbSource.create().useConnectionPrivider(() -> conn);

        OneEntityQuerier<Document> querier = Sql.from(Document.class).where(d -> {
            cond(d.getId(), "=", 111);
            cond(d.getTitle(), "=", "zzz");
        });
        SqlQueryException e = assertThrows(SqlQueryException.class, () -> querier.queryList());
        assertEquals("SELECT * FROM Document t1 WHERE t1.id = ? AND t1.title = ?", e.getSql());
        assertEquals(Arrays.asList(111, "zzz"), e.getParamValues());
        assertEquals("SELECT * FROM Document t1 WHERE t1.id = 111 AND t1.title = 'zzz'", e.getValuedSql());
        assertEquals("Error on query : SELECT * FROM Document t1 WHERE t1.id = 111 AND t1.title = 'zzz'",
                e.getMessage());
    }
}
