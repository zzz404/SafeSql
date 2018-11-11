package zzz404.safesql;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class InCondition extends Condition {

    private Object[] values;

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

}
