package zzz404.safesql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
        return Utils.isEquals(this, that,
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
    protected int do_setValueToPstmt_and_returnNextIndex(int i,
            PreparedStatement pstmt) {
        try {
            pstmt.setObject(i++, value);
        }
        catch (SQLException e) {
            throw Utils.throwRuntime(e);
        }
        return i;
    }

}
