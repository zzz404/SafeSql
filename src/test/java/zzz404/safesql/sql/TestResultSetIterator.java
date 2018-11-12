package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

import zzz404.safesql.Utils;

public class TestResultSetIterator {

    @Test
    public void test_hasNext_noData_return_false() {
        ResultSet rs = data(new String[0]);

        ResultSetIterator iter = new ResultSetIterator(rs);
        assertFalse(iter.hasNext());
        assertFalse(iter.hasNext());
        assertFalse(iter.hasNext());
    }

    @Test
    public void test_next_noData_throw_NoSuchElementException() {
        ResultSet rs = data(new String[0]);

        ResultSetIterator iter = new ResultSetIterator(rs);
        try {
            iter.next();
            fail();
        }
        catch (NoSuchElementException e) {
        }
    }

    @Test
    public void test_hasNext_hasData_alwaysReturn_true() {
        ResultSet rs = data(new String[] { "aaa", "bbb" });

        ResultSetIterator iter = new ResultSetIterator(rs);
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
    }

    @Test
    public void test_next_hasTwoData_returnTwice() throws SQLException {
        ResultSet rs = data(new String[] { "aaa", "bbb" });

        ResultSetIterator iter = new ResultSetIterator(rs);

        assertEquals("aaa", iter.next().getString(1));
        assertEquals("bbb", iter.next().getString(1));

        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_next_normalOrder() throws SQLException {
        ResultSet rs = data(new String[] { "aaa", "bbb" });

        ResultSetIterator iter = new ResultSetIterator(rs);

        assertTrue(iter.hasNext());
        assertEquals("aaa", iter.next().getString(1));

        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));

        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_next_abnormalOrder() throws SQLException {
        ResultSet rs0 = data(new String[] { "aaa", "bbb" });

        ResultSetIterator iter = new ResultSetIterator(rs0);

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
    public void test_hasNext_limited() {
        ResultSet rs = data(new String[] { "aaa", "bbb" });

        ResultSetIterator iter = new ResultSetIterator(rs, 10, 2);

        assertFalse(iter.hasNext());
    }

    @Test
    public void test_next_wrongRange_throw_NoSuchElementException() {
        ResultSet rs = data(new String[] { "aaa", "bbb" });

        ResultSetIterator iter = new ResultSetIterator(rs, 10, 2);
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_next_limited_normalOrder() throws SQLException {
        ResultSet rs = data(new String[] { "aaa", "bbb", "ccc", "ddd" });

        ResultSetIterator iter = new ResultSetIterator(rs, 1, 2);

        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));

        assertTrue(iter.hasNext());
        assertEquals("ccc", iter.next().getString(1));

        assertFalse(iter.hasNext());
    }

    @Test
    public void test_hasNext_next_limited_truncated() throws SQLException {
        ResultSet rs = data(new String[] { "aaa", "bbb" });

        ResultSetIterator iter = new ResultSetIterator(rs, 1, 10);

        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));

        assertFalse(iter.hasNext());
    }

    @Test
    public static ResultSet data(String[] data) {
        try {
            ResultSet rs = mock(ResultSet.class);
            int[] index = new int[] { -1 };
            when(rs.next()).thenAnswer(i -> {
                index[0]++;
                return (index[0] < data.length);
            });
            when(rs.getString(1)).thenAnswer(i -> {
                if (index[0] >= 0 && index[0] < data.length) {
                    return data[index[0]];
                }
                else {
                    throw new SQLException();
                }
            });
            when(rs.absolute(anyInt())).then(i -> {
                int position = (Integer) i.getArguments()[0];
                if (position < data.length) {
                    index[0] = position - 1;
                }
                else {
                    index[0] = position - 1;
                }
                return position > 0 && position <= data.length;
            });
            return rs;
        }
        catch (SQLException e) {
            throw Utils.throwRuntime(e);
        }
    }

}