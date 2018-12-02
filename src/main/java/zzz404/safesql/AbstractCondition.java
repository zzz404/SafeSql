package zzz404.safesql;

import static zzz404.safesql.Sql.*;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public abstract class AbstractCondition implements Condition {

    protected Field field;

    protected AbstractCondition(Field field) {
        this.field = field;
    }

    public static <T> AbstractCondition of(Field tableField, String operator, Object... values) {
        if (operator.equals(BETWEEN)) {
            Validate.isTrue(values.length == 2);
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

    public <T> AbstractCondition or(T field, String operator, Object... values) {
        QueryContext ctx = QueryContext.get();
        AbstractCondition cond = AbstractCondition.of(ctx.takeField(), operator, values);
        cond = new OrCondition(this, cond);

        ctx.addCondition(cond);
        return cond;
    }

    public abstract String toClause();

    public abstract void appendValuesTo(List<Object> paramValues);

    public void appendUsedEntitiesTo(Set<Entity<?>> entities) {
        entities.add(field.getEntity());
    }

}
