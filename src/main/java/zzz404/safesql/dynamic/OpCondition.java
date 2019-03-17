package zzz404.safesql.dynamic;

import java.util.List;

import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public class OpCondition extends AbstractCondition {

    private String operator;
    private TypedValue<?> value;

    public OpCondition(FieldImpl field, String operator, Object value) {
        super(field);
        this.operator = operator;
        if (value != null) {
            this.value = TypedValue.valueOf(value);
        }
        else {
            this.value = TypedValue.valueOf(field.getValueClass());
        }
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
