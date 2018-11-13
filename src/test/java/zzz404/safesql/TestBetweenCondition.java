package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class TestBetweenCondition {

    @Test
    void test_toClause() {
        BetweenCondition cond = new BetweenCondition("zzz", 123, 456);
        assertEquals("zzz BETWEEN ? AND ?", cond.toClause());
    }

    @Test
    void test__setValueToPstmt_and_returnNextIndex() throws SQLException {
        BetweenCondition cond = new BetweenCondition("zzz", 123, 456);
        PreparedStatement pstmt = mock(PreparedStatement.class);

        int nextIndex = cond.do_setValueToPstmt_and_returnNextIndex(1, pstmt);
        assertEquals(3, nextIndex);

        InOrder inOrder = inOrder(pstmt);

        inOrder.verify(pstmt, times(1)).setObject(1, 123);
        inOrder.verify(pstmt, times(1)).setObject(2, 456);
    }

}
