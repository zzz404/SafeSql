package zzz404.safesql;

import zzz404.safesql.sql.QuietPreparedStatement;

public class OpCondition extends Condition {

    private String operator;
    private Object value;

    public OpCondition(String columnName, String operator, Object value) {
        super(columnName);
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that,
                o -> new Object[] { o.columnName, o.operator, o.value });
    }

    @Override
    public String toString() {
        return "OpCondition [field=" + columnName + ", operator=" + operator
                + ", value=" + value + "]";
    }

    @Override
    public String toClause() {
        return columnName + " " + operator + " ?";
    }

    @Override
    protected int setValueToPstmt_and_returnNextIndex(int i,
            QuietPreparedStatement pstmt) {
        pstmt.setObject(i++, value);
        return i;
    }

}
