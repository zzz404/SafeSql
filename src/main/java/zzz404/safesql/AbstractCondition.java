package zzz404.safesql;

import static zzz404.safesql.Sql.*;

import java.util.List;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.sql.type.TypedValue;

public abstract class AbstractCondition implements Condition {

    protected Field<?> field;

    protected AbstractCondition(Field<?> field) {
        this.field = field;
    }

    public static <T> AbstractCondition of(Field<T> tableField, String operator,
            @SuppressWarnings("unchecked") T... values) {
        if (operator.equals(BETWEEN)) {
            Validate.isTrue(values.length == 2);
            return new BetweenCondition<T>(tableField, values[0], values[1]);
        }
        else if (operator.equals(IN)) {
            return new InCondition<T>(tableField, values);
        }
        else {
            Validate.isTrue(values.length == 1);
            return new OpCondition<T>(tableField, operator, values[0]);
        }
    }

    @Override
    public <T> OrCondition or(T fieldValue, String operator, @SuppressWarnings("unchecked") T... values) {
        QueryContext ctx = QueryContext.get();
        @SuppressWarnings("unchecked")
        Field<T> field = (Field<T>) ctx.takeField();
        AbstractCondition cond = AbstractCondition.of(field, operator, values);
        OrCondition orCond = new OrCondition(this, cond);
        ctx.reaplaceLastCondition(orCond);
        return orCond;
    }

    public abstract String toClause();

    public abstract void appendValuesTo(List<TypedValue<?>> paramValues);

}
