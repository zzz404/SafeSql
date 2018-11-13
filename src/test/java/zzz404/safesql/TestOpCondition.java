package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class TestOpCondition {

    @Test
    void test_toClause() {
        OpCondition cond = new OpCondition("zzz", "=", 123);
        assertEquals("zzz = ?", cond.toClause());
    }

    @Test
    void test__setValueToPstmt_and_returnNextIndex() throws SQLException {
        OpCondition cond = new OpCondition("zzz", "=", 123);
        PreparedStatement pstmt = mock(PreparedStatement.class);

        int nextIndex = cond.setValueToPstmt_and_returnNextIndex(11, pstmt);
        assertEquals(12, nextIndex);
        verify(pstmt, times(1)).setObject(11, 123);
    }

}
