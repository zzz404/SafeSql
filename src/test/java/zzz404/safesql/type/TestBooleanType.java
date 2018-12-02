package zzz404.safesql.type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.sql.QuietResultSet;

public class TestBooleanType {

    @Test
    void test_mapRsToObject_Boolean() {
        QuietResultSet rs = mock(QuietResultSet.class);
        when(rs.getBoolean(1)).thenReturn(true);
        assertEquals(Boolean.TRUE, ValueType.mapRsRowToObject(rs, Boolean.class));
        assertEquals(Boolean.TRUE, ValueType.mapRsRowToObject(rs, boolean.class));
        
        when(rs.getBoolean("zzz")).thenReturn(true);
        assertEquals(Boolean.TRUE, ValueType.mapRsRowToObject(rs, Boolean.class));
        assertEquals(Boolean.TRUE, ValueType.mapRsRowToObject(rs, boolean.class));
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
    void test_valueToString_boolean() {
        assertEquals("true", ValueType.valueToString(true));
    }

}
