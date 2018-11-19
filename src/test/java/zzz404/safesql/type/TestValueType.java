package zzz404.safesql.type;

import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import zzz404.safesql.sql.QuietPreparedStatement;

public class TestValueType {

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
        
        Date d = new Date(System.currentTimeMillis());
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

}
