package zzz404.safesql.dynamic;

import zzz404.safesql.util.CommonUtils;

public class OrderBy {
    private FieldImpl field;
    private boolean isAsc;

    public OrderBy(FieldImpl field, boolean isAsc) {
        this.field = field;
        this.isAsc = isAsc;
    }

    public String toClause() {
        String column = field.getPrefixedColumnName();
        return column + " " + (isAsc ? "ASC" : "DESC");
    }

    @Override
    public String toString() {
        return toClause();
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.field, o.isAsc });
    }

}
