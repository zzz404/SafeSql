package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import zzz404.safesql.sql.QuietPreparedStatement;

class TestOrCondition {

    private static OpCondition cond1 = new OpCondition("a", "=", 2);
    private static OpCondition cond2 = new OpCondition("b", "<>", "4");

    @Test
    void test_toClause() {
        OrCondition cond = new OrCondition(cond1, cond2);
        assertEquals("(a = ? OR b <> ?)", cond.toClause());
    }

    @Test
    void test__setValueToPstmt_and_returnNextIndex() throws SQLException {
        OrCondition cond = new OrCondition(cond1, cond2);

        QuietPreparedStatement pstmt = mock(QuietPreparedStatement.class);
        InOrder inOrder = inOrder(pstmt);

        int nextIndex = cond.setValueToPstmt_and_returnNextIndex(1, pstmt);
        assertEquals(3, nextIndex);

        inOrder.verify(pstmt, times(1)).setObject(1, 2);
        inOrder.verify(pstmt, times(1)).setObject(2, "4");
    }

    @Test
    void coverRest() {
        OrCondition orCond = new OrCondition(new OpCondition("", "", ""),
                new OpCondition("", "", ""));
        orCond.toString();
        orCond.equals(orCond);
    }
}
