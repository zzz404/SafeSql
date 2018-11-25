package zzz404.safesql;

import java.util.List;

import zzz404.safesql.util.CommonUtils;

public class MutualCondition extends AbstractCondition {

    private String operator;
    protected TableField tableColumn2;

    public MutualCondition(TableField tableColumn, String operator, TableField tableColumn2) {
        super(tableColumn);
        this.operator = operator;
        this.tableColumn2 = tableColumn2;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.tableColumn, o.operator, o.tableColumn2 });
    }

    @Override
    public String toString() {
        return toClause();
    }

    @Override
    public String toClause() {
        return tableColumn + " " + operator + " ?";
    }

    @Override
    public void appendValuesTo(List<Object> paramValues) {
    }

    public TableField getTableColumn2() {
        return tableColumn2;
    }

}
