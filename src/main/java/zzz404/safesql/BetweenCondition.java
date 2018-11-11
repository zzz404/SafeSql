package zzz404.safesql;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class BetweenCondition extends Condition {

    private Object value1;
    private Object value2;

    public BetweenCondition(Object value1, Object value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public BetweenCondition(String field, Object value1, Object value2) {
        this(value1, value2);
        this.field = field;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        BetweenCondition that = (BetweenCondition) o;
        return new EqualsBuilder().append(this.field, that.field)
                .append(this.value1, that.value1)
                .append(this.value2, that.value2).isEquals();
    }

    @Override
    public String toString() {
        return "BetweenCondition [field=" + field + ", value1=" + value1
                + ", value2=" + value2 + "]";
    }

}
