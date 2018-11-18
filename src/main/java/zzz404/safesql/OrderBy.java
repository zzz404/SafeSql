package zzz404.safesql;

import zzz404.safesql.util.CommonUtils;

public final class OrderBy {
    public TableColumn tableColumn;
    public boolean isAsc;

    public OrderBy(TableColumn tableColumn, boolean isAsc) {
        this.tableColumn = tableColumn;
        this.isAsc = isAsc;
    }

    public String toClause() {
        return tableColumn + " " + (isAsc ? "ASC" : "DESC");
    }

    @Override
    public String toString() {
        return toClause();
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.tableColumn, o.isAsc });
    }

}
