package zzz404.safesql;

import static zzz404.safesql.Sql.*;

import java.sql.PreparedStatement;

abstract class Condition {

    protected String columnName;

    protected Condition(String columnName) {
        this.columnName = columnName;
    }

    public static <T> Condition of(String columnName, String operator,
            Object... values) {
        if (operator.equals(BETWEEN)) {
            assert values.length == 2;
            return new BetweenCondition(columnName, values[0], values[1]);
        }
        else if (operator.equals(IN)) {
            return new InCondition(columnName, values);
        }
        else {
            assert values.length == 1;
            return new OpCondition(columnName, operator, values[0]);
        }
    }

    public <T> Condition or(T field, String operator, Object... values) {
        QueryContext ctx = QueryContext.INSTANCE.get();
        Condition cond = Condition.of(ctx.takeColumnName(), operator, values);
        cond = new OrCondition(this, cond);

        ctx.replaceLastCondition(cond);
        return cond;
    }

    public abstract String toClause();

    protected abstract int setValueToPstmt_and_returnNextIndex(int i, PreparedStatement pstmt);
    
}
