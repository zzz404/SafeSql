package zzz404.safesql;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.util.CommonUtils;

public class OpCondition extends Condition {

    private String operator;
    private Object value;

    public OpCondition(TableColumn tableColumn, String operator, Object value) {
        super(tableColumn);
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.tableColumn, o.operator, o.value });
    }

    @Override
    public String toString() {
        return "OpCondition [field=" + tableColumn + ", operator=" + operator + ", value=" + value + "]";
    }

    @Override
    public String toClause() {
        return tableColumn + " " + operator + " ?";
    }

    @Override
    protected int setValueToPstmt_and_returnNextIndex(int i, QuietPreparedStatement pstmt) {
        pstmt.setObject(i++, value);
        return i;
    }

}
