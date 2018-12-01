package zzz404.safesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import zzz404.safesql.util.CommonUtils;

public class OrCondition extends AbstractCondition {

    public List<AbstractCondition> subConditions;

    protected OrCondition(AbstractCondition... subConditions) {
        super(null);
        this.subConditions = new ArrayList<>();
        this.subConditions.addAll(Arrays.asList(subConditions));
    }

    public <T> OrCondition or(T field, String operator, Object... values) {
        QueryContext ctx = QueryContext.get();
        AbstractCondition cond = AbstractCondition.of(ctx.takeTableField(), operator, values);
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
    public void appendValuesTo(List<Object> paramValues) {
        for (AbstractCondition cond : subConditions) {
            cond.appendValuesTo(paramValues);
        }
    }

    @Override
    public void appendUsedEntitiesTo(Set<Entity<?>> entities) {
        for (AbstractCondition cond : subConditions) {
            cond.appendUsedEntitiesTo(entities);
        }
    }

}
