package zzz404.safesql;

import java.util.List;

import zzz404.safesql.util.CommonUtils;

public class BetweenCondition extends AbstractCondition {

    private Object value1;
    private Object value2;

    public BetweenCondition(Field tableColumn, Object value1, Object value2) {
        super(tableColumn);
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return "BetweenCondition [field=" + field + ", value1=" + value1 + ", value2=" + value2 + "]";
    }

    @Override
    public String toClause() {
        return field.getPrefixedRealColumnName() + " BETWEEN ? AND ?";
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.field, o.value1, o.value2 });
    }

    @Override
    public void appendValuesTo(List<Object> paramValues) {
        paramValues.add(value1);
        paramValues.add(value2);
    }

}
