package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import zzz404.safesql.sql.QuietPreparedStatement;

class TestStaticSqlQuerier {

    @Test
    void test_buildSql() {
        String sql = "aaa";
        StaticSqlQuerier q = new StaticSqlQuerier(sql);
        assertEquals(sql, q.buildSql());
    }

    @Test
    void test_buildSql_for_queryCount() {
        String sql_hasOrderBy = "select aaa,bbb, ccc FROM ddd where aaa=1 order BY eee";
        StaticSqlQuerier q = new StaticSqlQuerier(sql_hasOrderBy);
        assertEquals("SELECT COUNT(*) FROM ddd where aaa=1", q.buildSql_for_queryCount());

        String sql_noOrderBy = "select aaa,bbb, ccc FROM ddd where aaa=1 ";
        q = new StaticSqlQuerier(sql_noOrderBy);
        assertEquals("SELECT COUNT(*) FROM ddd where aaa=1 ", q.buildSql_for_queryCount());
    }

    @Test
    void test_setCondValueToPstmt() {
        StaticSqlQuerier q = new StaticSqlQuerier("Hello").paramValues(123, "zzz");
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        q.setCondValueToPstmt(pstmt);

        InOrder inOrder = inOrder(pstmt);
        inOrder.verify(pstmt, times(1)).setObject(1, 123);
        inOrder.verify(pstmt, times(1)).setObject(2, "zzz");
    }

    @Test
    void coverRest() {
        new StaticSqlQuerier("Hello").offset(0).limit(0);
    }
}
