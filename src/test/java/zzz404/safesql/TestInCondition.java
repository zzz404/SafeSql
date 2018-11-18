package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import zzz404.safesql.sql.QuietPreparedStatement;

class TestInCondition {

    private static final TableColumn column_zzz = new TableColumn(0, "zzz");

    @Test
    void test_toClause() {
        InCondition cond = new InCondition(column_zzz, 1, 2, 3);
        assertEquals("zzz IN (?, ?, ?)", cond.toClause());
    }

    @Test
    void test_toClause_noParams() {
        InCondition cond = new InCondition(column_zzz);
        assertEquals("0<>0", cond.toClause());
    }

    @Test
    void test__setValueToPstmt_and_returnNextIndex() throws SQLException {
        InCondition cond = new InCondition(column_zzz, 1, 3, 7);
        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);
        InOrder inOrder = inOrder(pstmt);

        int nextIndex = cond.setValueToPstmt_and_returnNextIndex(2, pstmt);
        assertEquals(5, nextIndex);

        inOrder.verify(pstmt, times(1)).setObject(2, 1);
        inOrder.verify(pstmt, times(1)).setObject(3, 3);
        inOrder.verify(pstmt, times(1)).setObject(4, 7);
    }

    @Test
    void coverRest() {
        new InCondition(new TableColumn(0, ""), "", "").toString();
    }
}
