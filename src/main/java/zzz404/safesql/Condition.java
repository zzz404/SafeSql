package zzz404.safesql;

import static zzz404.safesql.Sql.*;

import java.util.List;

import org.apache.commons.lang3.Validate;

abstract class Condition {

    protected TableColumn tableColumn;

    protected Condition(TableColumn tableColumn) {
        this.tableColumn = tableColumn;
    }

    public static <T> Condition of(TableColumn tableColumn, String operator, Object... values) {
        if (operator.equals(BETWEEN)) {
            Validate.isTrue(values.length == 2);
            return new BetweenCondition(tableColumn, values[0], values[1]);
        }
        else if (operator.equals(IN)) {
            return new InCondition(tableColumn, values);
        }
        else {
            Validate.isTrue(values.length == 1);
            return new OpCondition(tableColumn, operator, values[0]);
        }
    }

    public <T> Condition or(T field, String operator, Object... values) {
        QueryContext ctx = QueryContext.get();
        Condition cond = Condition.of(ctx.takeTableColumn(), operator, values);
        cond = new OrCondition(this, cond);

        ctx.replaceLastCondition(cond);
        return cond;
    }

    public abstract String toClause();

    protected abstract void appendValuesTo(List<Object> paramValues);

}
