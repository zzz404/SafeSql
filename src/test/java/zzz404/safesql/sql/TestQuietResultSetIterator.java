package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.FakeDatabase;

public class TestQuietResultSetIterator {

    @Test
    public void test_hasNext_noData_return_false() throws SQLException {
        QuietResultSet rs = fakeRs();

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);
        assertFalse(iter.hasNext());
        assertFalse(iter.hasNext());
        assertFalse(iter.hasNext());
    }

    @Test
    public void test_next_noData_throw_NoSuchElementException() throws SQLException {
        QuietResultSet rs = fakeRs(new String[0]);

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_hasData_alwaysReturn_true() throws SQLException {
        QuietResultSet rs = fakeRs("aaa", "bbb");

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
    }

    @Test
    public void test_next_hasTwoData_returnTwice() throws SQLException {
        QuietResultSet rs = fakeRs("aaa", "bbb");

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);

        assertEquals("aaa", iter.next().getString(1));
        assertEquals("bbb", iter.next().getString(1));

        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_next_normalOrder() throws SQLException {
        QuietResultSet rs = fakeRs("aaa", "bbb");

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);

        assertTrue(iter.hasNext());
        assertEquals("aaa", iter.next().getString(1));

        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));

        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_next_abnormalOrder() throws SQLException {
        QuietResultSet rs0 = fakeRs("aaa", "bbb");

        QuietResultSetIterator iter = new QuietResultSetIterator(rs0);

        assertEquals("aaa", iter.next().getString(1));
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));
        assertFalse(iter.hasNext());
        assertFalse(iter.hasNext());
        assertFalse(iter.hasNext());

        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_limited() throws SQLException {
        QuietResultSet rs = fakeRs("aaa", "bbb");

        QuietResultSetIterator iter = new QuietResultSetIterator(rs, 0, 1);

        assertTrue(iter.hasNext());
        assertEquals("aaa", iter.next().getString(1));
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_next_wrongRange_throw_NoSuchElementException() throws SQLException {
        QuietResultSet rs = fakeRs("aaa", "bbb");

        QuietResultSetIterator iter = new QuietResultSetIterator(rs, 10, 2);
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    private QuietResultSet fakeRs(String... values) throws SQLException {
        ResultSet rs = new FakeDatabase().pushSingleColumnData(values).getNextResultSet();
        return new QuietResultSet(rs);
    }

    @Test
    public void test_hasNext_next_limited_normalOrder() throws SQLException {
        QuietResultSet rs = fakeRs("aaa", "bbb", "ccc", "ddd");

        QuietResultSetIterator iter = new QuietResultSetIterator(rs, 1, 2);

        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));

        assertTrue(iter.hasNext());
        assertEquals("ccc", iter.next().getString(1));

        assertFalse(iter.hasNext());
    }

    @Test
    public void test_hasNext_next_limited_truncated() throws SQLException {
        QuietResultSet rs = fakeRs("aaa", "bbb");

        QuietResultSetIterator iter = new QuietResultSetIterator(rs, 1, 10);

        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));

        assertFalse(iter.hasNext());
    }

}