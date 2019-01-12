package zzz404.safesql.dynamic;

import java.util.List;

import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public class BetweenCondition extends AbstractCondition {

    private TypedValue<?> value1;
    private TypedValue<?> value2;

    public BetweenCondition(FieldImpl tableColumn, Object value1, Object value2) {
        super(tableColumn);
        this.value1 = TypedValue.valueOf(value1);
        this.value2 = TypedValue.valueOf(value2);
    }

    @Override
    public String toString() {
        return "BetweenCondition [field=" + field + ", value1=" + value1 + ", value2=" + value2 + "]";
    }

    @Override
    public String toClause() {
        return field.getPrefixedColumnName() + " BETWEEN ? AND ?";
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.field, o.value1, o.value2 });
    }

    @Override
    public void appendValuesTo(List<TypedValue<?>> paramValues) {
        paramValues.add(value1);
        paramValues.add(value2);
    }

}
