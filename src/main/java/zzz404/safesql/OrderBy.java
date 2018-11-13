package zzz404.safesql;

public final class OrderBy implements EqualsSupport {
    public String field;
    public boolean isAsc;

    public OrderBy(String field, boolean isAsc) {
        this.field = field;
        this.isAsc = isAsc;
    }

    public OrderBy(boolean isAsc) {
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
    public Object[] equalsByValues() {
        return new Object[] { field, isAsc };
    }

}
