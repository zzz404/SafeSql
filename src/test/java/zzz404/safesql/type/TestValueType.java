package zzz404.safesql.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.util.NoisySupplier;

public class TestValueType {
    private static final String DATE_STR = "2018-11-19";
    private static final String TIME_STR = "21:18:05";
    private static final String DATE_TIME_STR = DATE_STR + " " + TIME_STR;
    private static final java.util.Date DATE_TIME = NoisySupplier.getQuietly(() -> {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(DATE_TIME_STR);
    });

    @Test
    void test_setValueToPstmt_index() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);
        ValueType.setValueToPstmt(pstmt, 123, 1);
        verify(pstmt).setInt(123, 1);
    }

    @Test
    void test_setValueToPstmt_int() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        ValueType.setValueToPstmt(pstmt, 1, 111);
        verify(pstmt).setInt(1, 111);

        ValueType.setValueToPstmt(pstmt, 1, new Integer(222));
        verify(pstmt).setInt(1, 222);
    }

    @Test
    void test_setValueToPstmt_String() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);
        ValueType.setValueToPstmt(pstmt, 1, "qaz");
        verify(pstmt).setString(1, "qaz");
    }

    @Test
    void test_setValueToPstmt_boolean() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        ValueType.setValueToPstmt(pstmt, 1, true);
        verify(pstmt).setBoolean(1, true);

        ValueType.setValueToPstmt(pstmt, 1, new Boolean(false));
        verify(pstmt).setBoolean(1, false);
    }

    @Test
    void test_setValueToPstmt_long() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        ValueType.setValueToPstmt(pstmt, 1, 12L);
        verify(pstmt).setLong(1, 12L);

        ValueType.setValueToPstmt(pstmt, 1, new Long(123));
        verify(pstmt).setLong(1, 123L);
    }

    @Test
    void test_setValueToPstmt_sqlDate() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        java.sql.Date d = new java.sql.Date(System.currentTimeMillis());
        ValueType.setValueToPstmt(pstmt, 1, d);
        verify(pstmt).setDate(1, d);
    }

    @Test
    void test_setValueToPstmt_double() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        ValueType.setValueToPstmt(pstmt, 1, 1.2);
        verify(pstmt).setDouble(1, 1.2);

        ValueType.setValueToPstmt(pstmt, 1, new Double(1.11));
        verify(pstmt).setDouble(1, 1.11);
    }

    @Test
    void test_setValueToPstmt_float() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        ValueType.setValueToPstmt(pstmt, 1, 1.2f);
        verify(pstmt).setFloat(1, 1.2f);

        ValueType.setValueToPstmt(pstmt, 1, new Float(1.11f));
        verify(pstmt).setFloat(1, 1.11f);
    }

    @Test
    void test_setValueToPstmt_short() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        ValueType.setValueToPstmt(pstmt, 1, (short) 12);
        verify(pstmt).setShort(1, (short) 12);

        ValueType.setValueToPstmt(pstmt, 1, new Short((short) 123));
        verify(pstmt).setShort(1, (short) 123);
    }

    @Test
    void test_setValueToPstmt_Time() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        Time t = new Time(System.currentTimeMillis());
        ValueType.setValueToPstmt(pstmt, 1, t);
        verify(pstmt).setTime(1, t);
    }

    @Test
    void test_setValueToPstmt_Timestamp() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        Timestamp t = new Timestamp(System.currentTimeMillis());
        ValueType.setValueToPstmt(pstmt, 1, t);
        verify(pstmt).setTimestamp(1, t);
    }

    @Test
    void test_setValueToPstmt_utilDate() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        java.util.Date d = new java.util.Date();
        ValueType.setValueToPstmt(pstmt, 1, d);
        verify(pstmt).setTimestamp(1, new Timestamp(d.getTime()));
    }

    @Test
    void test_setValueToPstmt_Instant() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        java.util.Date d = new java.util.Date();
        ValueType.setValueToPstmt(pstmt, 1, d.toInstant());
        verify(pstmt).setTimestamp(1, new Timestamp(d.getTime()));
    }

    @Test
    void test_setValueToPstmt_Object() {
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        Object o = new Object();
        ValueType.setValueToPstmt(pstmt, 1, o);
        verify(pstmt).setObject(1, o);

        Object o2 = new TestValueType();
        ValueType.setValueToPstmt(pstmt, 1, o2);
        verify(pstmt).setObject(1, o2);

        ValueType.setValueToPstmt(pstmt, 1, null);
        verify(pstmt).setObject(1, null);
    }

    @Test
    void test_valueToString_int() {
        assertEquals("123", ValueType.valueToString(123));
    }

    @Test
    void test_valueToString_String() {
        assertEquals("'abc'", ValueType.valueToString("abc"));
    }

    @Test
    void test_valueToString_boolean() {
        assertEquals("true", ValueType.valueToString(true));
    }

    @Test
    void test_valueToString_long() {
        assertEquals("11", ValueType.valueToString(11L));
    }

    @Test
    void test_valueToString_sqlDate() {
        assertEquals(DATE_STR, ValueType.valueToString(new java.sql.Date(DATE_TIME.getTime())));
    }

    @Test
    void test_valueToString_double() {
        assertEquals("1.23", ValueType.valueToString(1.23));
    }

    @Test
    void test_valueToString_float() {
        assertEquals("1.23", ValueType.valueToString(1.23f));
    }

    @Test
    void test_valueToString_short() {
        assertEquals("123", ValueType.valueToString((short) 123));
    }

    @Test
    void test_valueToString_Time() {
        assertEquals(TIME_STR, ValueType.valueToString(new Time(DATE_TIME.getTime())));
    }

    @Test
    void test_valueToString_Timestamp() {
        assertEquals(DATE_TIME_STR, ValueType.valueToString(new Timestamp(DATE_TIME.getTime())));
    }

    @Test
    void test_valueToString_utilDate() {
        assertEquals(DATE_TIME_STR, ValueType.valueToString(DATE_TIME));
    }

    @Test
    void test_valueToString_Instant() {
        assertEquals(DATE_TIME_STR, ValueType.valueToString(DATE_TIME.toInstant()));
    }

    @Test
    void test_valueToString_null() {
        assertEquals("null", ValueType.valueToString(null));
    }

    @Test
    void test_valueToString_Object() {
        String s = "To be? Or not to be? It's a question.";
        class Zzz {
            @Override
            public String toString() {
                return s;
            }
        }
        assertEquals(s, ValueType.valueToString(new Zzz()));
    }

    @Test
    void test_mapRsRowToObject_Integer() {
        QuietResultSet rs = mock(QuietResultSet.class);

        when(rs.getInt(1)).thenReturn(3);
        assertEquals(new Integer(3), ValueType.mapRsRowToObject(rs, Integer.class));
        assertEquals(new Integer(3), ValueType.mapRsRowToObject(rs, int.class));

        when(rs.getInt("zzz")).thenReturn(5);
        assertEquals(new Integer(5), ValueType.valueOf(Integer.class).readFromRs(rs, "zzz"));
        assertEquals(new Integer(5), ValueType.valueOf(int.class).readFromRs(rs, "zzz"));

        when(rs.getInt(1)).thenReturn(0);
        when(rs.wasNull()).thenReturn(true);
        assertNull(ValueType.mapRsRowToObject(rs, Integer.class));
        assertNull(ValueType.mapRsRowToObject(rs, int.class));
    }

    @Test
    void test_mapRsRowToObject_String() {
        QuietResultSet rs = mock(QuietResultSet.class);

        when(rs.getString(1)).thenReturn("zxc");
        assertEquals("zxc", ValueType.mapRsRowToObject(rs, String.class));

        when(rs.getString("zzz")).thenReturn("zxc");
        assertEquals("zxc", ValueType.valueOf(String.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Boolean() {
        QuietResultSet rs = mock(QuietResultSet.class);

        when(rs.getBoolean(1)).thenReturn(true);
        assertEquals(Boolean.TRUE, ValueType.mapRsRowToObject(rs, Boolean.class));
        assertEquals(Boolean.TRUE, ValueType.mapRsRowToObject(rs, boolean.class));

        when(rs.getBoolean("zzz")).thenReturn(false);
        assertEquals(Boolean.FALSE, ValueType.valueOf(Boolean.class).readFromRs(rs, "zzz"));
        assertEquals(Boolean.FALSE, ValueType.valueOf(boolean.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Date() {
        QuietResultSet rs = mock(QuietResultSet.class);
        Date date = new Date(System.currentTimeMillis());

        when(rs.getDate(1)).thenReturn(date);
        assertEquals(date, ValueType.mapRsRowToObject(rs, Date.class));

        when(rs.getDate("zzz")).thenReturn(date);
        assertEquals(date, ValueType.valueOf(Date.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Double() {
        QuietResultSet rs = mock(QuietResultSet.class);

        when(rs.getDouble(1)).thenReturn(1.2);
        assertEquals(new Double(1.2), ValueType.mapRsRowToObject(rs, Double.class));
        assertEquals(new Double(1.2), ValueType.mapRsRowToObject(rs, double.class));

        when(rs.getDouble("zzz")).thenReturn(2.1);
        assertEquals(new Double(2.1), ValueType.valueOf(Double.class).readFromRs(rs, "zzz"));
        assertEquals(new Double(2.1), ValueType.valueOf(double.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Float() {
        QuietResultSet rs = mock(QuietResultSet.class);

        when(rs.getFloat(1)).thenReturn(1.2f);
        assertEquals(new Float(1.2f), ValueType.mapRsRowToObject(rs, Float.class));
        assertEquals(new Float(1.2f), ValueType.mapRsRowToObject(rs, float.class));

        when(rs.getFloat("zzz")).thenReturn(2.1f);
        assertEquals(new Float(2.1f), ValueType.valueOf(Float.class).readFromRs(rs, "zzz"));
        assertEquals(new Float(2.1f), ValueType.valueOf(float.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Long() {
        QuietResultSet rs = mock(QuietResultSet.class);

        when(rs.getLong(1)).thenReturn(3L);
        assertEquals(new Long(3), ValueType.mapRsRowToObject(rs, Long.class));
        assertEquals(new Long(3), ValueType.mapRsRowToObject(rs, long.class));

        when(rs.getLong("zzz")).thenReturn(7L);
        assertEquals(new Long(7), ValueType.valueOf(Long.class).readFromRs(rs, "zzz"));
        assertEquals(new Long(7), ValueType.valueOf(long.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Short() {
        QuietResultSet rs = mock(QuietResultSet.class);

        when(rs.getShort(1)).thenReturn((short) 3);
        assertEquals(new Short((short) 3), ValueType.mapRsRowToObject(rs, Short.class));
        assertEquals(new Short((short) 3), ValueType.mapRsRowToObject(rs, short.class));

        when(rs.getShort("zzz")).thenReturn((short) 2);
        assertEquals(new Short((short) 2), ValueType.valueOf(Short.class).readFromRs(rs, "zzz"));
        assertEquals(new Short((short) 2), ValueType.valueOf(short.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Time() {
        QuietResultSet rs = mock(QuietResultSet.class);
        Time time = new Time(System.currentTimeMillis());

        when(rs.getTime(1)).thenReturn(time);
        assertEquals(time, ValueType.mapRsRowToObject(rs, Time.class));

        when(rs.getTime("zzz")).thenReturn(time);
        assertEquals(time, ValueType.valueOf(Time.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Timestamp() {
        QuietResultSet rs = mock(QuietResultSet.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp, ValueType.mapRsRowToObject(rs, Timestamp.class));

        when(rs.getTimestamp("zzz")).thenReturn(timestamp);
        assertEquals(timestamp, ValueType.valueOf(Timestamp.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_utilDate() {
        QuietResultSet rs = mock(QuietResultSet.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp, ValueType.mapRsRowToObject(rs, java.util.Date.class));

        when(rs.getTimestamp("zzz")).thenReturn(timestamp);
        assertEquals(timestamp, ValueType.valueOf(java.util.Date.class).readFromRs(rs, "zzz"));
    }

    @Test
    void test_mapRsRowToObject_Instant() {
        QuietResultSet rs = mock(QuietResultSet.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp.toInstant(), ValueType.mapRsRowToObject(rs, Instant.class));

        when(rs.getTimestamp("zzz")).thenReturn(timestamp);
        assertEquals(timestamp.toInstant(), ValueType.valueOf(Instant.class).readFromRs(rs, "zzz"));

        when(rs.getTimestamp(1)).thenReturn(null);
        assertNull(ValueType.mapRsRowToObject(rs, Instant.class));
    }

    @Test
    void test_mapRsRowToObject_unknownType() {
        QuietResultSet rs = mock(QuietResultSet.class);
        assertNull(ValueType.mapRsRowToObject(rs, this.getClass()));
    }

}
