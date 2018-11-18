package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.UtilsForTest;
import zzz404.safesql.sql.QuietPreparedStatement;

class TestSqlQuerier {

    private FakeDatabase packet;

    @BeforeEach
    void beforeEach() {
        ConnectionFactory.create().setConnectionPrivider(() -> packet.getConnection());
        QueryContext.create("");
        packet = new FakeDatabase();
    }

    @AfterEach
    void afterEach() {
        QueryContext.clear();
        ConnectionFactory.map.clear();
    }

    @Test
    void test_queryCount() {
        packet.pushData(7);
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
        packet.pushData(3, 6);
        SqlQuerier q = new MySqlQuerier();
        Optional<Integer> result = q.queryOne(Integer.class);
        assertEquals(3, result.get().intValue());
    }

    @Test
    void test_queryOne_offset() {
        packet.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier().offset(4);
        Optional<Integer> result = q.queryOne(Integer.class);
        assertEquals(5, result.get().intValue());
    }

    @Test
    void test_queryList() {
        packet.pushData(7, 5);
        SqlQuerier q = new MySqlQuerier();
        List<Integer> result = q.queryList(Integer.class);
        assertTrue(UtilsForTest.isEquals(result, 7, 5));
    }

    @Test
    void test_queryList_offset() {
        packet.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier().offset(4).limit(5);
        List<Integer> result = q.queryList(Integer.class);
        assertTrue(UtilsForTest.isEquals(result, 5, 9));
    }

    @Test
    void test_queryPage() {
        packet.pushData(6);
        packet.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier().offset(2).limit(3);
        Page<Integer> page = q.queryPage(Integer.class);
        assertEquals(6, page.getTotalCount());
        assertTrue(UtilsForTest.isEquals(page.getResult(), 4, 1, 5));
    }

    @Test
    void test_queryStream_of_ResultSet() {
        packet.pushData("a", "b", "c");
        SqlQuerier q = new MySqlQuerier();
        String text = q.queryStream(stream -> stream.map(rs -> rs.getString(1)).collect(Collectors.joining(",")));
        assertEquals("a,b,c", text);
    }

    @Test
    void test_queryStream_of_Object() {
        packet.pushData("a", "b", "c");
        SqlQuerier q = new MySqlQuerier();
        String text = q.queryStream(String.class, stream -> stream.collect(Collectors.joining(",")));
        assertEquals("a,b,c", text);
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
