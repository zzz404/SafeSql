package zzz404.safesql;

public final class OrderBy {
    public String field;
    public boolean isAsc;

    public OrderBy(String field, boolean isAsc) {
        this.field = field;
        this.isAsc = isAsc;
    }

    public String toClause() {
        return field + " " + (isAsc ? "ASC" : "DESC");
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
