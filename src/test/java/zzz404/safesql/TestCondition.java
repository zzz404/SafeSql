package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.UtilsForTest;

class TestCondition {
    private static final TableField column_z = UtilsForTest.createTableField("z");

    @Test
    void test_of_createConditionByOperator() {
        AbstractCondition cond = AbstractCondition.of(column_z, "=", 1);
        assertTrue(cond instanceof OpCondition);

        cond = AbstractCondition.of(column_z, BETWEEN, 1, 2);
        assertTrue(cond instanceof BetweenCondition);

        cond = AbstractCondition.of(column_z, IN, 1, 2);
        assertTrue(cond instanceof InCondition);
    }

    @Test
    void coverRest() {
        UtilsForTest.pass(() -> AbstractCondition.of(column_z, "=", 1, 2));
        UtilsForTest.pass(() -> AbstractCondition.of(column_z, BETWEEN, 1));
    }

}
