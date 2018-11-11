package zzz404.safesql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class InCondition extends Condition {

    private Object[] values = null;

    public InCondition(String columnName, Object... values) {
        super(columnName);
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        InCondition that = (InCondition) o;
        return new EqualsBuilder().append(this.columnName, that.columnName)
                .append(this.values, that.values).isEquals();
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
            PreparedStatement pstmt) {
        try {
            for (Object value : values) {
                pstmt.setObject(i++, value);
            }
        }
        catch (SQLException e) {
            throw Utils.throwRuntime(e);
        }
        return i;
    }

}
