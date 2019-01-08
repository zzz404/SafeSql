package zzz404.safesql.dynamic;

import java.util.List;

import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public class OpCondition<T> extends AbstractCondition {

    private String operator;
    private TypedValue<?> value;

    public OpCondition(FieldImpl<T> field, String operator, T value) {
        super(field);
        this.operator = operator;
        this.value = TypedValue.valueOf(value);
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
        return field.getPrefixedColumnName() + " " + operator + " ?";
    }

    @Override
    public void appendValuesTo(List<TypedValue<?>> paramValues) {
        paramValues.add(value);
    }

}
