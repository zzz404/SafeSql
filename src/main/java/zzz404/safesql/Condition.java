package zzz404.safesql;

import static zzz404.safesql.Sql.*;

import java.util.Iterator;

abstract class Condition {

    protected String field;

    public static Condition of(String operator, Object... values) {
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

    public <T> Condition or(T field, String operator, Object... values) {
        QueryContext builder = QueryContext.instance.get();

        Condition cond = Condition.of(operator, values);
        cond = new OrCondition(this, cond);

        builder.replaceLastCondition(cond);
        return cond;
    }

    protected void fillField(Iterator<String> field_iter) {
        this.field = field_iter.next();
    }

}
