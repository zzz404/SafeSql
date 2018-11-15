package zzz404.safesql;

import static org.junit.jupiter.api.Assertions.*;
import static zzz404.safesql.Sql.*;

import org.junit.jupiter.api.Test;

import zzz404.safesql.helper.TestUtils;

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
    void coverRest() {
        TestUtils.pass(() -> Condition.of("z", "=", 1, 2));
        TestUtils.pass(() -> Condition.of("z", BETWEEN, 1));
    }

}
