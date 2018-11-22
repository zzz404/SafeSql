package zzz404.safesql;

import zzz404.safesql.util.CommonUtils;

public final class OrderBy {
    public String prefixedColumnName;
    public boolean isAsc;

    public OrderBy(String prefixedColumnName, boolean isAsc) {
        this.prefixedColumnName = prefixedColumnName;
        this.isAsc = isAsc;
    }

    public String toClause() {
        return prefixedColumnName + " " + (isAsc ? "ASC" : "DESC");
    }

    @Override
    public String toString() {
        return toClause();
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.prefixedColumnName, o.isAsc });
    }

}
