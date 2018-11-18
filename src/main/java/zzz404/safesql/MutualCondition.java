package zzz404.safesql;

import zzz404.safesql.sql.QuietPreparedStatement;
import zzz404.safesql.util.CommonUtils;

public class MutualCondition extends Condition {

    private String operator;
    private TableColumn tableColumn2;

    public MutualCondition(TableColumn tableColumn, String operator, TableColumn tableColumn2) {
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
    protected int setValueToPstmt_and_returnNextIndex(int i, QuietPreparedStatement pstmt) {
        return i;
    }

}
