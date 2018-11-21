package zzz404.safesql.sql.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.type.ValueType;

public class TestValueType {

    private QuietResultSet rs;

    @BeforeEach
    void beforeEach() {
        rs = mock(QuietResultSet.class);
    }

    @Test
    void test_mapRsRowToObject_Integer() {
        when(rs.getInt(1)).thenReturn(3);
        assertEquals(new Integer(3), ValueType.mapRsRowToObject(rs, Integer.class));
        assertEquals(new Integer(3), ValueType.mapRsRowToObject(rs, int.class));
    }

    @Test
    void test_mapRsToObject_String() {
        when(rs.getString(1)).thenReturn("zxc");
        assertEquals("zxc", ValueType.mapRsRowToObject(rs, String.class));
    }

    @Test
    void test_mapRsToObject_Boolean() {
        when(rs.getBoolean(1)).thenReturn(true);
        assertEquals(Boolean.TRUE, ValueType.mapRsRowToObject(rs, Boolean.class));
        assertEquals(Boolean.TRUE, ValueType.mapRsRowToObject(rs, boolean.class));
    }

    @Test
    void test_mapRsToObject_Date() {
        Date date = new Date(System.currentTimeMillis());
        when(rs.getDate(1)).thenReturn(date);
        assertEquals(date, ValueType.mapRsRowToObject(rs, Date.class));
    }

    @Test
    void test_mapRsToObject_Double() {
        when(rs.getDouble(1)).thenReturn(1.2);
        assertEquals(new Double(1.2), ValueType.mapRsRowToObject(rs, Double.class));
        assertEquals(new Double(1.2), ValueType.mapRsRowToObject(rs, double.class));
    }

    @Test
    void test_mapRsToObject_Float() {
        when(rs.getFloat(1)).thenReturn(1.2f);
        assertEquals(new Float(1.2f), ValueType.mapRsRowToObject(rs, Float.class));
        assertEquals(new Float(1.2f), ValueType.mapRsRowToObject(rs, float.class));
    }

    @Test
    void test_mapRsToObject_Long() {
        when(rs.getLong(1)).thenReturn(3L);
        assertEquals(new Long(3), ValueType.mapRsRowToObject(rs, Long.class));
        assertEquals(new Long(3), ValueType.mapRsRowToObject(rs, long.class));
    }

    @Test
    void test_mapRsToObject_Short() {
        when(rs.getShort(1)).thenReturn((short) 3);
        assertEquals(new Short((short) 3), ValueType.mapRsRowToObject(rs, Short.class));
        assertEquals(new Short((short) 3), ValueType.mapRsRowToObject(rs, short.class));
    }

    @Test
    void test_mapRsToObject_Time() {
        Time time = new Time(System.currentTimeMillis());
        when(rs.getTime(1)).thenReturn(time);
        assertEquals(time, ValueType.mapRsRowToObject(rs, Time.class));
    }

    @Test
    void test_mapRsToObject_Timestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp, ValueType.mapRsRowToObject(rs, Timestamp.class));
    }

    @Test
    void test_mapRsToObject_utilDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp, ValueType.mapRsRowToObject(rs, java.util.Date.class));
    }

    @Test
    void test_mapRsToObject_Instant() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp.toInstant(), ValueType.mapRsRowToObject(rs, Instant.class));
    }

}
