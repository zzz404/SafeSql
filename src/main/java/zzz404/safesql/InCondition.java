package zzz404.safesql;

import java.util.Arrays;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class InCondition extends Condition {

    private Object[] values;

    public InCondition(Object... values) {
        this.values = values;
    }

    public InCondition(String field, Object... values) {
        this(values);
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        InCondition that = (InCondition) o;
        return new EqualsBuilder().append(this.field, that.field)
                .append(this.values, that.values).isEquals();
    }

    @Override
    public String toString() {
        return "InCondition [field=" + field + ", values="
                + Arrays.toString(values) + "]";
    }

}
