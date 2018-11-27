package zzz404.safesql;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import zzz404.safesql.util.CommonUtils;

public class InCondition extends AbstractCondition {

    private Object[] values = new Object[0];

    public InCondition(TableField tableColumn, Object... values) {
        super(tableColumn);
        this.values = values;
    }

    @Override
    public String toString() {
        return "InCondition [field=" + tableField + ", values=" + Arrays.toString(values) + "]";
    }

    @Override
    public String toClause() {
        if (values.length == 0) {
            return "0<>0";
        }
        else {
            return tableField + " IN ("
                    + Collections.nCopies(values.length, "?").stream().collect(Collectors.joining(", ")) + ")";
        }
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> o.values);
    }

    @Override
    public void appendValuesTo(List<Object> paramValues) {
        for (Object value : values) {
            paramValues.add(value);
        }
    }

}
