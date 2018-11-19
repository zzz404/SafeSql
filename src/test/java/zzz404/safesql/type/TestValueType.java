package zzz404.safesql.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.util.NoisySupplier;

public class TestValueType {
    private static final String DATE_STR = "2018-11-19";
    private static final String TIME_STR = "21:18:05";
    private static final String DATE_TIME_STR = DATE_STR + " " + TIME_STR;
    private static final java.util.Date DATE_TIME = NoisySupplier.getQuiet(() -> {
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
        assertEquals("abc", ValueType.valueToString("abc"));
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
        assertEquals("To be? Or not to be? It's a question.", ValueType.valueToString(new TestValueType()));
    }

    @Override
    public String toString() {
        return "To be? Or not to be? It's a question.";
    }

}
