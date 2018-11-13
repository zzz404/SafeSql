package zzz404.safesql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BetweenCondition extends Condition implements EqualsSupport {

    private Object value1;
    private Object value2;

    public BetweenCondition(String columnName, Object value1, Object value2) {
        super(columnName);
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public Object[] equalsByValues() {
        return new Object[] { columnName, value1, value2 };
    }

    @Override
    public String toString() {
        return "BetweenCondition [field=" + columnName + ", value1=" + value1
                + ", value2=" + value2 + "]";
    }

    @Override
    public String toClause() {
        return columnName + " BETWEEN ? AND ?";
    }

    @Override
    protected int do_setValueToPstmt_and_returnNextIndex(int i,
            PreparedStatement pstmt) {
        try {
            pstmt.setObject(i++, value1);
            pstmt.setObject(i++, value2);
        }
        catch (SQLException e) {
            throw Utils.throwRuntime(e);
        }
        return i;
    }

}
