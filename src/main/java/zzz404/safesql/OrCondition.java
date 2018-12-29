package zzz404.safesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public class OrCondition extends AbstractCondition {

    public List<AbstractCondition> subConditions;

    protected OrCondition(AbstractCondition... subConditions) {
        super(null);
        this.subConditions = new ArrayList<>();
        this.subConditions.addAll(Arrays.asList(subConditions));
    }

    @Override
    public <T> OrCondition or(T fieldValue, String operator, @SuppressWarnings("unchecked") T... values) {
        QueryContext ctx = QueryContext.get();
        @SuppressWarnings("unchecked")
        Field<T> field = (Field<T>) ctx.takeField();
        AbstractCondition cond = AbstractCondition.of(field, operator, values);
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
    public void appendValuesTo(List<TypedValue<?>> paramValues) {
        for (AbstractCondition cond : subConditions) {
            cond.appendValuesTo(paramValues);
        }
    }

}
