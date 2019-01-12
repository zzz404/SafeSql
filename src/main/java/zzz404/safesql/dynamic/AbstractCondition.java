package zzz404.safesql.dynamic;

import static zzz404.safesql.SafeSql.*;

import java.util.List;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.SafeSqlException;
import zzz404.safesql.sql.type.TypedValue;

public abstract class AbstractCondition implements Condition {

    protected FieldImpl field;

    protected AbstractCondition(FieldImpl field) {
        this.field = field;
    }

    public static AbstractCondition of(FieldImpl tableField, String operator, Object... values) {
        if (operator.equals(BETWEEN)) {
            if (values.length != 2) {
                throw new SafeSqlException("A BetweenCondition must has exact two parameters!");
            }
            return new BetweenCondition(tableField, values[0], values[1]);
        }
        else if (operator.equals(IN)) {
            return new InCondition(tableField, values);
        }
        else {
            Validate.isTrue(values.length == 1);
            return new OpCondition(tableField, operator, values[0]);
        }
    }

    @Override
    public OrCondition or(Object fieldValue, String operator, Object... values) {
        QueryContext ctx = QueryContext.get();
        FieldImpl field = ctx.takeField();
        AbstractCondition cond = AbstractCondition.of(field, operator, values);
        OrCondition orCond = new OrCondition(this, cond);
        ctx.reaplaceLastCondition(orCond);
        return orCond;
    }

    public abstract String toClause();

    public abstract void appendValuesTo(List<TypedValue<?>> paramValues);

}
