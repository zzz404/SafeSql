package zzz404.safesql;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.util.CommonUtils;

public class OrderBy {
    private Field tableField;
    private String columnName;
    private boolean isAsc;

    public OrderBy(String columnName, boolean isAsc) {
        this.columnName = columnName;
        this.isAsc = isAsc;
    }

    public OrderBy(Field tableField, boolean isAsc) {
        this.tableField = tableField;
        this.isAsc = isAsc;
    }

    public String toClause() {
        String column;
        if (tableField != null) {
            column = tableField.getPrefixedPropertyName();
        }
        else {
            column = columnName;
        }
        return column + " " + (isAsc ? "ASC" : "DESC");
    }

    public void setEntity(Entity<?> entity) {
        Validate.isTrue(tableField == null);
        tableField = new Field(entity, columnName);
    }

    @Override
    public String toString() {
        return toClause();
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.columnName, o.isAsc });
    }

    public Field getTableField() {
        return tableField;
    }

    public String getColumnName() {
        return columnName;
    }

}
