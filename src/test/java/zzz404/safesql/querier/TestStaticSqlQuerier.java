package zzz404.safesql.querier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.StaticSqlExecuter;

class TestStaticSqlQuerier {

    @Test
    void test_buildSql() {
        String sql = "aaa";
        StaticSqlExecuter q = new StaticSqlExecuter(null).sql(sql);
        assertEquals(sql, q.sql());
    }

    @Test
    void test_buildSql_for_queryCount() {
        String sql_hasOrderBy = "select aaa,bbb, ccc FROM ddd where aaa=1 order BY eee";
        StaticSqlExecuter q = new StaticSqlExecuter(null).sql(sql_hasOrderBy);
        assertEquals("SELECT COUNT(*) FROM ddd where aaa=1", q.sql_for_queryCount());

        String sql_noOrderBy = "select aaa,bbb, ccc FROM ddd where aaa=1 ";
        q = new StaticSqlExecuter(null).sql(sql_noOrderBy);
        assertEquals("SELECT COUNT(*) FROM ddd where aaa=1 ", q.sql_for_queryCount());
    }

    @Test
    void test_setCondValueToPstmt() {
        StaticSqlExecuter q = new StaticSqlExecuter(null).sql("Hello").paramValues(123, "zzz");
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        q.setCondsValueToPstmt(pstmt);

        InOrder inOrder = inOrder(pstmt);
        inOrder.verify(pstmt, times(1)).setInt(1, 123);
        inOrder.verify(pstmt, times(1)).setString(2, "zzz");
    }

    @Test
    void cover_rest() {
        new StaticSqlExecuter(null).sql("Hello").offset(0).limit(0);
    }
}
