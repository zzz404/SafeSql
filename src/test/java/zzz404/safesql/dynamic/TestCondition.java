package zzz404.safesql.dynamic;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.dynamic.AbstractCondition;
import zzz404.safesql.dynamic.BetweenCondition;
import zzz404.safesql.dynamic.FieldImpl;
import zzz404.safesql.dynamic.InCondition;
import zzz404.safesql.dynamic.OpCondition;
import zzz404.safesql.helper.UtilsForTest;

class TestCondition {
    private static final FieldImpl<Integer> column_z = UtilsForTest.createSimpleField("z");

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
    void test_of_errorArguments() {
        assertThrows(IllegalArgumentException.class, () -> AbstractCondition.of(column_z, "=", 1, 2));
        assertThrows(IllegalArgumentException.class, () -> AbstractCondition.of(column_z, BETWEEN, 1));
    }

}
