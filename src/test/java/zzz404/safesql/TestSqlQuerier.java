package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.FakeDatabase;

class TestSqlQuerier {

    private FakeDatabase fakeDb;

    @BeforeEach
    void beforeEach() {
        ConnectionFactory.create(() -> fakeDb.getConnection());
        fakeDb = new FakeDatabase();
    }

    @AfterEach
    void afterEach() {
        ConnectionFactory.map.clear();
    }

    @Test
    void test_queryCount() {
        fakeDb.pushData(7);
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
        fakeDb.pushData(3, 6);
        SqlQuerier q = new MySqlQuerier();
        Optional<Integer> result = q.queryOne(Integer.class);
        assertEquals(3, result.get().intValue());
    }

    @Test
    void test_queryOne_offset() {
        fakeDb.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier().offset(4);
        Optional<Integer> result = q.queryOne(Integer.class);
        assertEquals(5, result.get().intValue());
    }

    @Test
    void test_queryList() {
        fakeDb.pushData(7, 5);
        SqlQuerier q = new MySqlQuerier();
        List<Integer> result = q.queryList(Integer.class);
        assertEquals(Arrays.asList(7, 5), result);
    }

    @Test
    void test_queryList_offset() {
        fakeDb.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier().offset(4).limit(5);
        List<Integer> result = q.queryList(Integer.class);
        assertEquals(Arrays.asList(5, 9), result);
    }

    @Test
    void test_queryPage() {
        fakeDb.pushData(6);
        fakeDb.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier().offset(2).limit(3);
        Page<Integer> page = q.queryPage(Integer.class);
        assertEquals(6, page.getTotalCount());

        assertEquals(Arrays.asList(4, 1, 5), page.getResult());
    }

    @Test
    void test_queryStream_of_ResultSet() {
        fakeDb.pushData("a", "b", "c");
        SqlQuerier q = new MySqlQuerier();
        String text = q.queryStream(stream -> stream.map(rs -> rs.getString(1)).collect(Collectors.joining(",")));
        assertEquals("a,b,c", text);
    }

    @Test
    void test_queryStream_of_Object() {
        fakeDb.pushData("a", "b", "c");
        SqlQuerier q = new MySqlQuerier();
        String text = q.queryStream(String.class, stream -> stream.collect(Collectors.joining(",")));
        assertEquals("a,b,c", text);
    }

    public static class MySqlQuerier extends SqlQuerier {

        public MySqlQuerier() {
            super("");
        }

        @Override
        protected String sql() {
            return "";
        }

        @Override
        protected String sql_for_queryCount() {
            return "";
        }

        @Override
        protected Object[] paramValues() {
            return new Object[0];
        }

    }
}
