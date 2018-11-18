package zzz404.safesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.util.CommonUtils;

public class OrCondition extends Condition {

    public List<Condition> subConditions;

    protected OrCondition(Condition... subConditions) {
        super(null);
        this.subConditions = new ArrayList<>();
        this.subConditions.addAll(Arrays.asList(subConditions));
    }

    public <T> OrCondition or(T field, String operator, Object... values) {
        QueryContext ctx = QueryContext.get();
        Condition cond = Condition.of(ctx.takeTableColumn(), operator, values);
        subConditions.add(cond);
        return this;
    }

    @Override
    public String toClause() {
        return "(" + subConditions.stream().map(c -> c.toClause()).collect(Collectors.joining(" OR ")) + ")";
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> subConditions.toArray());
    }

    @Override
    protected void appendValuesTo(List<Object> paramValues) {
        for (Condition cond : subConditions) {
            cond.appendValuesTo(paramValues);
        }
    }

}
