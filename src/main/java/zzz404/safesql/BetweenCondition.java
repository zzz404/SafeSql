package zzz404.safesql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class BetweenCondition extends Condition {

    private Object value1;
    private Object value2;

    public BetweenCondition(String columnName, Object value1, Object value2) {
        super(columnName);
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        BetweenCondition that = (BetweenCondition) o;
        return new EqualsBuilder().append(this.columnName, that.columnName)
                .append(this.value1, that.value1)
                .append(this.value2, that.value2).isEquals();
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
    protected int setValueToPstmt_and_returnNextIndex(int i,
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
