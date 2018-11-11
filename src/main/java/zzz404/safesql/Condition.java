package zzz404.safesql;

import static zzz404.safesql.Sql.*;

abstract class Condition {

    protected String field;

    protected Condition() {
    }

    protected Condition(String field) {
        this.field = field;
    }

    public static Condition of(String operator,
            Object... values) {
        if (operator.equals(BETWEEN)) {
            assert values.length == 2;
            return new BetweenCondition(values[0], values[1]);
        }
        else if (operator.equals(IN)) {
            return new InCondition(values);
        }
        else {
            assert values.length == 1;
            return new OpCondition(operator, values[0]);
        }
    }
    
    public static Condition of(String field, String operator,
            Object... values) {
        if (operator.equals(BETWEEN)) {
            assert values.length == 2;
            return new BetweenCondition(field, values[0], values[1]);
        }
        else if (operator.equals(IN)) {
            return new InCondition(field, values);
        }
        else {
            assert values.length == 1;
            return new OpCondition(field, operator, values[0]);
        }
    }

}
