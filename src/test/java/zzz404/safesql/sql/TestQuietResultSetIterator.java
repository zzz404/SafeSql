package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class TestQuietResultSetIterator {

    @Test
    public void test_hasNext_noData_return_false() {
        QuietResultSet rs = data(new String[0]);

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);
        assertFalse(iter.hasNext());
        assertFalse(iter.hasNext());
        assertFalse(iter.hasNext());
    }

    @Test
    public void test_next_noData_throw_NoSuchElementException() {
        QuietResultSet rs = data(new String[0]);

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_hasData_alwaysReturn_true() {
        QuietResultSet rs = data(new String[] { "aaa", "bbb" });

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
        assertTrue(iter.hasNext());
    }

    @Test
    public void test_next_hasTwoData_returnTwice() throws SQLException {
        QuietResultSet rs = data(new String[] { "aaa", "bbb" });

        QuietResultSetIterator iter = new QuietResultSetIterator(rs);

        assertEquals("aaa", iter.next().getString(1));
        assertEquals("bbb", iter.next().getString(1));

        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_next_normalOrder() throws SQLException {
        QuietResultSet rs = data(new String[] { "aaa", "bbb" });

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
        QuietResultSet rs0 = data(new String[] { "aaa", "bbb" });

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
    public void test_hasNext_limited() {
        QuietResultSet rs = data(new String[] { "aaa", "bbb" });

        QuietResultSetIterator iter = new QuietResultSetIterator(rs, 0, 1);

        assertTrue(iter.hasNext());
        assertEquals("aaa", iter.next().getString(1));
        assertFalse(iter.hasNext());
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_next_wrongRange_throw_NoSuchElementException() {
        QuietResultSet rs = data(new String[] { "aaa", "bbb" });

        QuietResultSetIterator iter = new QuietResultSetIterator(rs, 10, 2);
        assertThrows(NoSuchElementException.class, () -> iter.next());
    }

    @Test
    public void test_hasNext_next_limited_normalOrder() throws SQLException {
        QuietResultSet rs = data(new String[] { "aaa", "bbb", "ccc", "ddd" });

        QuietResultSetIterator iter = new QuietResultSetIterator(rs, 1, 2);

        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));

        assertTrue(iter.hasNext());
        assertEquals("ccc", iter.next().getString(1));

        assertFalse(iter.hasNext());
    }

    @Test
    public void test_hasNext_next_limited_truncated() throws SQLException {
        QuietResultSet rs = data(new String[] { "aaa", "bbb" });

        QuietResultSetIterator iter = new QuietResultSetIterator(rs, 1, 10);

        assertTrue(iter.hasNext());
        assertEquals("bbb", iter.next().getString(1));

        assertFalse(iter.hasNext());
    }

    @Test
    public static QuietResultSet data(String[] data) {
        QuietResultSet rs = mock(QuietResultSet.class);
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

}