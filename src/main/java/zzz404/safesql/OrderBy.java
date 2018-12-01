package zzz404.safesql;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.util.CommonUtils;

public class OrderBy {
    private Field field;
    private String columnName;
    private boolean isAsc;

    public OrderBy(String columnName, boolean isAsc) {
        this.columnName = columnName;
        this.isAsc = isAsc;
    }

    public OrderBy(Field field, boolean isAsc) {
        this.field = field;
        this.isAsc = isAsc;
    }

    public String toClause() {
        String column;
        if (field != null) {
            column = field.getPrefixedPropertyName();
        }
        else {
            column = columnName;
        }
        return column + " " + (isAsc ? "ASC" : "DESC");
    }

    public void setEntity(Entity<?> entity) {
        Validate.isTrue(field == null);
        field = new Field(entity, columnName);
    }

    @Override
    public String toString() {
        return toClause();
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.columnName, o.isAsc });
    }

    public Field getField() {
        return field;
    }

    public String getColumnName() {
        return columnName;
    }

}
