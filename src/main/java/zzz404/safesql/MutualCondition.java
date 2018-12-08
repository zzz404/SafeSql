package zzz404.safesql;

import java.util.List;
import java.util.Set;

import zzz404.safesql.util.CommonUtils;

public class MutualCondition extends AbstractCondition {

    private String operator;
    protected Field field2;

    public MutualCondition(Field tableColumn, String operator, Field tableColumn2) {
        super(tableColumn);
        this.operator = operator;
        this.field2 = tableColumn2;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.field, o.operator, o.field2 });
    }

    @Override
    public String toString() {
        return toClause();
    }

    @Override
    public String toClause() {
        return field.getPrefixedRealColumnName() + " " + operator + " " + field2.getPrefixedRealColumnName();
    }

    @Override
    public void appendValuesTo(List<Object> paramValues) {
    }

    public Field getField2() {
        return field2;
    }

    @Override
    public void appendUsedEntitiesTo(Set<Entity<?>> entities) {
        super.appendUsedEntitiesTo(entities);
        entities.add(field2.getEntity());
    }

}
