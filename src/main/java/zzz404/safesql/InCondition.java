package zzz404.safesql;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import zzz404.safesql.sql.QuietPreparedStatement;

public class InCondition extends Condition {

    private Object[] values = null;

    public InCondition(String columnName, Object... values) {
        super(columnName);
        this.values = values;
    }

    @Override
    public String toString() {
        return "InCondition [field=" + columnName + ", values="
                + Arrays.toString(values) + "]";
    }

    @Override
    public String toClause() {
        if (values.length == 0) {
            return "0<>0";
        }
        else {
            return columnName + " IN ("
                    + Collections.nCopies(values.length, "?").stream()
                            .collect(Collectors.joining(", "))
                    + ")";
        }
    }

    @Override
    protected int setValueToPstmt_and_returnNextIndex(int i,
            QuietPreparedStatement pstmt) {
        for (Object value : values) {
            pstmt.setObject(i++, value);
        }
        return i;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> o.values);
    }

}
