package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static zzz404.safesql.Sql.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

class TestCondition {

    @Test
    void test_of_createConditionByOperator() {
        Condition cond = Condition.of("z", "=", 1);
        assertTrue(cond instanceof OpCondition);

        cond = Condition.of("z", BETWEEN, 1, 2);
        assertTrue(cond instanceof BetweenCondition);

        cond = Condition.of("z", IN, 1, 2);
        assertTrue(cond instanceof InCondition);
    }

    @Test
    void coverage() throws SQLException {
        Condition cond = Condition.of("z", "=", 1);

        PreparedStatement pstmt = mock(PreparedStatement.class);
        doThrow(SQLException.class).when(pstmt).setObject(11, 1);

        assertThrows(SQLException.class,
                () -> cond.setValueToPstmt_and_returnNextIndex(11, pstmt));
    }

}
