package zzz404.safesql;

import zzz404.safesql.util.CommonUtils;

public final class TableColumn {
    private int tableIndex;
    private String columnName;

    public TableColumn(int tableIndex, String columnName) {
        this.tableIndex = tableIndex;
        this.columnName = columnName;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getPrefixedColumnName() {
        if (tableIndex > 0) {
            return "t" + tableIndex + "." + columnName;
        }
        else {
            return columnName;
        }
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, tc -> new Object[] { tc.tableIndex, tc.columnName });
    }

    @Override
    public String toString() {
        return getPrefixedColumnName();
    }

}