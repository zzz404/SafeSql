package zzz404.safesql;

import java.util.List;

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
    protected void appendValuesTo(List<Object> paramValues) {
        paramValues.add(value);
    }

}
