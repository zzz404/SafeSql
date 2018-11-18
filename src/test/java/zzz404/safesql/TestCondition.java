package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;

class TestCondition {
    private static final TableColumn column_z = new TableColumn(0, "z");

    @Test
    void test_of_createConditionByOperator() {
        Condition cond = Condition.of(column_z, "=", 1);
        assertTrue(cond instanceof OpCondition);

        cond = Condition.of(column_z, BETWEEN, 1, 2);
        assertTrue(cond instanceof BetweenCondition);

        cond = Condition.of(column_z, IN, 1, 2);
        assertTrue(cond instanceof InCondition);
    }

    @Test
    void coverRest() {
        UtilsForTest.pass(() -> Condition.of(column_z, "=", 1, 2));
        UtilsForTest.pass(() -> Condition.of(column_z, BETWEEN, 1));
    }

}
