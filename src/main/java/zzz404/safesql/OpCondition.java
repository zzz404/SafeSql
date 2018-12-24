package zzz404.safesql;

import java.util.List;

import zzz404.safesql.util.CommonUtils;

public class OpCondition extends AbstractCondition {

    private String operator;
    private Object value;

    public OpCondition(Field field, String operator, Object value) {
        super(field);
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.field, o.operator, o.value });
    }

    @Override
    public String toString() {
        return "OpCondition [field=" + field + ", operator=" + operator + ", value=" + value + "]";
    }

    @Override
    public String toClause() {
        return field.getPrefixedRealColumnName() + " " + operator + " ?";
    }

    @Override
    public void appendValuesTo(List<Object> paramValues) {
        paramValues.add(value);
    }

}
