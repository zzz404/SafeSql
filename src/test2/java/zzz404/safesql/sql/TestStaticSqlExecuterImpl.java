package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestStaticSqlExecuterImpl {

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
    void cover_rest() {
        new StaticSqlExecuter(null).sql("Hello").offset(0).limit(0);
    }
}
