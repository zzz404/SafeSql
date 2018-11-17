package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.JdbcPacket;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.QuietPreparedStatement;

class TestSqlQuerier {

    private JdbcPacket packet;

    @BeforeEach
    void beforeEach() {
        ConnectionFactory.create();
        packet = new JdbcPacket();
        ConnectionFactory.get()
                .setConnectionPrivider(() -> packet.getConnection());
    }

    @AfterEach
    void afterEach() {
        ConnectionFactory.map.clear();
    }

    @Test
    void test_queryCount() {
        packet.bindData(7);
        SqlQuerier q = new MySqlQuerier();
        assertEquals(7, q.queryCount());
    }

    @Test
    void test_queryOne_noData_returnNothing() {
        SqlQuerier q = new MySqlQuerier();
        Optional<Integer> result = q.queryOne(Integer.class);
        assertFalse(result.isPresent());
    }

    @Test
    void test_queryOne_twoData_returnFirst() {
        packet.bindData(3, 6);
        SqlQuerier q = new MySqlQuerier();
        Optional<Integer> result = q.queryOne(Integer.class);
        assertEquals(3, result.get().intValue());
    }

    @Test
    void test_queryOne_offset() {
        packet.bindData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier().offset(4);
        Optional<Integer> result = q.queryOne(Integer.class);
        assertEquals(5, result.get().intValue());
    }

    @Test
    void test_queryList() {
        packet.bindData(7, 5);
        MySqlQuerier q = new MySqlQuerier();
        List<Integer> result = q.queryList(Integer.class);
        assertTrue(UtilsForTest.isEquals(result, 7, 5));
    }

    public static class MySqlQuerier extends SqlQuerier {

        @Override
        protected String buildSql() {
            return "";
        }

        @Override
        protected String buildSql_for_queryCount() {
            return "";
        }

        @Override
        protected void setCondValueToPstmt(QuietPreparedStatement pstmt) {
        }

    }
}
