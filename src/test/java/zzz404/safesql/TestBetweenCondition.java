package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import zzz404.safesql.sql.QuietPreparedStatement;

class TestBetweenCondition {

    @Test
    void test_toClause() {
        BetweenCondition cond = new BetweenCondition(new TableColumn(0, "zzz"), 123, 456);
        assertEquals("zzz BETWEEN ? AND ?", cond.toClause());
    }

    @Test
    void test__setValueToPstmt_and_returnNextIndex() throws SQLException {
        BetweenCondition cond = new BetweenCondition(new TableColumn(0, "zzz"), 123, 456);
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);

        int nextIndex = cond.setValueToPstmt_and_returnNextIndex(1, pstmt);
        assertEquals(3, nextIndex);

        InOrder inOrder = inOrder(pstmt);

        inOrder.verify(pstmt, times(1)).setObject(1, 123);
        inOrder.verify(pstmt, times(1)).setObject(2, 456);
    }

    @Test
    void coverRest() {
        new BetweenCondition(new TableColumn(0, "zzz"), 123, 456).toString();
    }
}
