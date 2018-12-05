package zzz404.safesql.querier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.ConnFactoryBackDoor;
import zzz404.safesql.Page;
import zzz404.safesql.helper.FakeDatabase;
import zzz404.safesql.helper.FakeDbSource;

class TestSqlQuerier {

    private FakeDatabase fakeDb;

    @BeforeEach
    void beforeEach() {
        fakeDb = new FakeDatabase();
    }

    @AfterEach
    void afterEach() {
        ConnFactoryBackDoor.removeAllFactories();
    }

    @Test
    void test_queryCount() {
        fakeDb.pushData(7);
        SqlQuerier q = new MySqlQuerier(fakeDb);
        assertEquals(7, q.queryCount());
    }

    @Test
    void test_queryOne_noData_returnNothing() {
        SqlQuerier q = new MySqlQuerier(fakeDb);
        Optional<Integer> result = q.queryOne(Integer.class);
        assertFalse(result.isPresent());
    }

    @Test
    void test_queryOne_twoData_returnFirst() {
        fakeDb.pushData(3, 6);
        SqlQuerier q = new MySqlQuerier(fakeDb);
        Optional<Integer> result = q.queryOne(Integer.class);
        assertEquals(3, result.get().intValue());
    }

    @Test
    void test_queryOne_offset() {
        fakeDb.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier(fakeDb).offset(4);
        Optional<Integer> result = q.queryOne(Integer.class);
        assertEquals(5, result.get().intValue());
    }

    @Test
    void test_queryList() {
        fakeDb.pushData(7, 5);
        SqlQuerier q = new MySqlQuerier(fakeDb);
        List<Integer> result = q.queryList(Integer.class);
        assertEquals(Arrays.asList(7, 5), result);
    }

    @Test
    void test_queryList_offset() {
        fakeDb.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier(fakeDb).offset(4).limit(5);
        List<Integer> result = q.queryList(Integer.class);
        assertEquals(Arrays.asList(5, 9), result);
    }

    @Test
    void test_queryPage() {
        fakeDb.pushData(6);
        fakeDb.pushData(3, 1, 4, 1, 5, 9);
        SqlQuerier q = new MySqlQuerier(fakeDb).offset(2).limit(3);
        Page<Integer> page = q.queryPage(Integer.class);
        assertEquals(6, page.getTotalCount());

        assertEquals(Arrays.asList(4, 1, 5), page.getResult());
    }

    @Test
    void test_queryStream_of_ResultSet() {
        fakeDb.pushData("a", "b", "c");
        SqlQuerier q = new MySqlQuerier(fakeDb);
        String text = q.queryStream(stream -> stream.map(rs -> rs.getString(1)).collect(Collectors.joining(",")));
        assertEquals("a,b,c", text);
    }

    @Test
    void test_queryStream_of_Object() {
        fakeDb.pushData("a", "b", "c");
        SqlQuerier q = new MySqlQuerier(fakeDb);
        String text = q.queryStream(String.class, stream -> stream.collect(Collectors.joining(",")));
        assertEquals("a,b,c", text);
    }

    @Test
    void test_statementCreated_noOffset() throws SQLException {
        SqlQuerier q = new MySqlQuerier(fakeDb);
        q.queryList(Integer.class);
        verify(fakeDb.conn, times(1)).createStatement();
        verify(fakeDb.stmt, times(1)).executeQuery("");
    }

    @Test
    void test_statementCreated_withOffset() throws SQLException {
        SqlQuerier q = new MySqlQuerier(fakeDb);
        q.offset(3).queryList(Integer.class);
        verify(fakeDb.conn, times(1)).createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        verify(fakeDb.stmt, times(1)).executeQuery("");
    }

    @Test
    void test_preparedStatementCreated_noOffset() throws SQLException {
        SqlQuerier q = new MySqlQuerier(fakeDb).paramValues(1, 2);
        q.queryList(Integer.class);
        verify(fakeDb.conn, times(1)).prepareStatement("");
        verify(fakeDb.pstmt, times(1)).executeQuery();
    }

    @Test
    void test_preparedStatementCreated_withOffset() throws SQLException {
        SqlQuerier q = new MySqlQuerier(fakeDb).paramValues(1, 2);
        q.offset(3).queryList(Integer.class);
        verify(fakeDb.conn, times(1)).prepareStatement("", ResultSet.TYPE_SCROLL_INSENSITIVE,
                ResultSet.CONCUR_READ_ONLY);
        verify(fakeDb.pstmt, times(1)).executeQuery();
    }

    public static class MySqlQuerier extends SqlQuerier {
        private Object[] paramValues = new Object[0];

        public MySqlQuerier(FakeDatabase fakeDb) {
            super(new FakeDbSource(fakeDb));
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
            return paramValues;
        }

        public MySqlQuerier paramValues(Object... paramValues) {
            this.paramValues = paramValues;
            return this;
        }
    }
}
