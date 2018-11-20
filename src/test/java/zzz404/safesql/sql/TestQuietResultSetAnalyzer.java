package zzz404.safesql.sql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestQuietResultSetAnalyzer {

    private QuietResultSet rs;
    private QuietResultSetAnalyzer analyzer;

    @BeforeEach
    void beforeEach() {
        rs = mock(QuietResultSet.class);
        analyzer = new QuietResultSetAnalyzer(rs);
    }

    @Test
    void test_mapRsToObject_Integer() {
        when(rs.getInt(1)).thenReturn(3);
        assertEquals(new Integer(3), analyzer.mapRsToObject(Integer.class, null));
        assertEquals(new Integer(3), analyzer.mapRsToObject(int.class, null));
    }

    @Test
    void test_mapRsToObject_String() {
        when(rs.getString(1)).thenReturn("zxc");
        assertEquals("zxc", analyzer.mapRsToObject(String.class, null));
    }

    @Test
    void test_mapRsToObject_Boolean() {
        when(rs.getBoolean(1)).thenReturn(true);
        assertEquals(Boolean.TRUE, analyzer.mapRsToObject(Boolean.class, null));
        assertEquals(Boolean.TRUE, analyzer.mapRsToObject(boolean.class, null));
    }

    @Test
    void test_mapRsToObject_Date() {
        Date date = new Date(System.currentTimeMillis());
        when(rs.getDate(1)).thenReturn(date);
        assertEquals(date, analyzer.mapRsToObject(Date.class, null));
    }

    @Test
    void test_mapRsToObject_Double() {
        when(rs.getDouble(1)).thenReturn(1.2);
        assertEquals(new Double(1.2), analyzer.mapRsToObject(Double.class, null));
        assertEquals(new Double(1.2), analyzer.mapRsToObject(double.class, null));
    }

    @Test
    void test_mapRsToObject_Float() {
        when(rs.getFloat(1)).thenReturn(1.2f);
        assertEquals(new Float(1.2f), analyzer.mapRsToObject(Float.class, null));
        assertEquals(new Float(1.2f), analyzer.mapRsToObject(float.class, null));
    }

    @Test
    void test_mapRsToObject_Long() {
        when(rs.getLong(1)).thenReturn(3L);
        assertEquals(new Long(3), analyzer.mapRsToObject(Long.class, null));
        assertEquals(new Long(3), analyzer.mapRsToObject(long.class, null));
    }

    @Test
    void test_mapRsToObject_Short() {
        when(rs.getShort(1)).thenReturn((short) 3);
        assertEquals(new Short((short) 3), analyzer.mapRsToObject(Short.class, null));
        assertEquals(new Short((short) 3), analyzer.mapRsToObject(short.class, null));
    }

    @Test
    void test_mapRsToObject_Time() {
        Time time = new Time(System.currentTimeMillis());
        when(rs.getTime(1)).thenReturn(time);
        assertEquals(time, analyzer.mapRsToObject(Time.class, null));
    }

    @Test
    void test_mapRsToObject_Timestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp, analyzer.mapRsToObject(Timestamp.class, null));
    }

    @Test
    void test_mapRsToObject_utilDate() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp, analyzer.mapRsToObject(java.util.Date.class, null));
    }

    @Test
    void test_mapRsToObject_Instant() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        when(rs.getTimestamp(1)).thenReturn(timestamp);
        assertEquals(timestamp.toInstant(), analyzer.mapRsToObject(Instant.class, null));
    }

}
