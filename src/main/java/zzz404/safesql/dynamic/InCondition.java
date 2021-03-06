package zzz404.safesql.dynamic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public class InCondition extends AbstractCondition {

    private List<TypedValue<?>> values;

    public InCondition(FieldImpl tableColumn, Object... values) {
        super(tableColumn);
        if (values == null) {
            values = new Object[0];
        }
        this.values = Arrays.stream(values).map(TypedValue::valueOf).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "InCondition [field=" + field + ", values=" + values + "]";
    }

    @Override
    public String toClause() {
        if (values.isEmpty()) {
            return "0<>0";
        }
        else {
            return field.getPrefixedColumnName() + " IN ("
                    + Collections.nCopies(values.size(), "?").stream().collect(Collectors.joining(", ")) + ")";
        }
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> o.values.toArray());
    }

    @Override
    public void appendValuesTo(List<TypedValue<?>> paramValues) {
        paramValues.addAll(values);
    }

}
