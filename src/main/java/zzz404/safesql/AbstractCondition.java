package zzz404.safesql;

import static zzz404.safesql.Sql.*;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.Validate;

public abstract class AbstractCondition implements Condition {

    protected TableField tableField;

    protected AbstractCondition(TableField tableField) {
        this.tableField = tableField;
    }

    public static <T> AbstractCondition of(TableField tableColumn, String operator, Object... values) {
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

    public <T> AbstractCondition or(T field, String operator, Object... values) {
        QueryContext ctx = QueryContext.get();
        AbstractCondition cond = AbstractCondition.of(ctx.takeTableColumn(), operator, values);
        cond = new OrCondition(this, cond);

        ctx.replaceLastCondition(cond);
        return cond;
    }

    public abstract String toClause();

    public abstract void appendValuesTo(List<Object> paramValues);

    public void appendUsedEntitiesTo(Set<Entity<?>> entities) {
        entities.add(tableField.getEntity());
    }

    public TableField getTableField() {
        return tableField;
    }

    @Override
    public <T> void as(T field) {
        QueryContext ctx = QueryContext.get();
        TableField column = ctx.takeTableColumn();
        ctx.addColumnMapping(tableField.getPropertyName(), column.getPropertyName());
    }

}
