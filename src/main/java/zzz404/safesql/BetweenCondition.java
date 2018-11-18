package zzz404.safesql;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.util.CommonUtils;

public class BetweenCondition extends Condition {

    private Object value1;
    private Object value2;

    public BetweenCondition(TableColumn tableColumn, Object value1, Object value2) {
        super(tableColumn);
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return "BetweenCondition [field=" + tableColumn + ", value1=" + value1 + ", value2=" + value2 + "]";
    }

    @Override
    public String toClause() {
        return tableColumn + " BETWEEN ? AND ?";
    }

    @Override
    protected int setValueToPstmt_and_returnNextIndex(int i, QuietPreparedStatement pstmt) {
        pstmt.setObject(i++, value1);
        pstmt.setObject(i++, value2);
        return i;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.tableColumn, o.value1, o.value2 });
    }

}
