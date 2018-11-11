package zzz404.safesql;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class OrCondition extends Condition {

    public List<Condition> subConditions;

    protected OrCondition(Condition... subConditions) {
        this.subConditions = Arrays.asList(subConditions);
    }

    public <T> Condition or(T field, String operator, Object... values) {
        Condition cond = Condition.of(operator, values);
        subConditions.add(cond);
        return this;
    }

    protected void fillField(Iterator<String> field_iter) {
        for (Condition cond : subConditions) {
            cond.fillField(field_iter);
        }
    }
}
