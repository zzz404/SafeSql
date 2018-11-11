package zzz404.safesql;

import org.apache.commons.lang3.builder.EqualsBuilder;

public final class OrderBy {
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
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        OrderBy that = (OrderBy) o;
        return new EqualsBuilder().append(this.field, that.field)
                .append(this.isAsc, that.isAsc).isEquals();
    }

}
