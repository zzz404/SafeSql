package zzz404.safesql;

import org.apache.commons.lang3.builder.EqualsBuilder;

public class OpCondition extends Condition {

    private String operator;
    private Object value;

    public OpCondition(String columnName, String operator, Object value) {
        super(columnName);
        this.operator = operator;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        OpCondition that = (OpCondition) o;
        return new EqualsBuilder().append(this.columnName, that.columnName)
                .append(this.operator, that.operator)
                .append(this.value, that.value).isEquals();
    }

    @Override
    public String toString() {
        return "OpCondition [field=" + columnName + ", operator=" + operator
                + ", value=" + value + "]";
    }

}
