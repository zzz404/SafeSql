package zzz404.safesql.dynamic;

import zzz404.safesql.Entity;
import zzz404.safesql.EntityGettable;
import zzz404.safesql.QueryContext;
import zzz404.safesql.sql.TableSchema;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public class Field<T> {
    private Entity<?> entity;
    private Class<T> clazz;
    private String propertyName;
    private String columnName;
    private String function;

    private Field<T> asField;

    public Field(Entity<?> entity, String propertyName) {
        this.entity = entity;
        this.propertyName = propertyName;
        this.columnName = propertyName;
        if (entity != null) {
            entity.addField(this);
        }
    }

    public Field(Entity<?> entity, String propertyName, Class<T> clazz) {
        this(entity, propertyName);
        this.clazz = clazz;
    }

    String getPropertyName() {
        return propertyName;
    }

    public String getPrefixedColumnName() {
        String result = columnName;
        if (entity != null && entity.getIndex() > 0) {
            result = "t" + entity.getIndex() + "." + result;
        }
        return result;
    }

    String getColumnClause() {
        String result = getPrefixedColumnName();
        if (function != null) {
            result = function + "(" + result + ")";
        }
        if (asField != null) {
            result += " AS " + asField.propertyName;
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void as(T o) {
        QueryContext ctx = QueryContext.get();
        asField = (Field<T>) ctx.takeLastField();
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that,
                tc -> new Object[] { tc.entity, tc.propertyName, tc.columnName, tc.function });
    }

    @Override
    public String toString() {
        return getPrefixedColumnName();
    }

    public static Field<Integer> count() {
        Field<Integer> field = new Field<>(null, "*", Integer.class);
        field.function = "COUNT";
        return field;
    }

    public static Field<?> all(EntityGettable mockedObject) {
        Entity<?> entity = mockedObject.entity();
        Field<?> field = new Field<>(entity, "*");
        return field;
    }

    public void revisedBy(TableSchema schema) {
        columnName = schema.getColumnName(propertyName);
    }

    void checkType() {
        if (clazz != null) {
            TypedValue.valueOf(clazz);
        }
    }

    String getColumnName() {
        return columnName;
    }

}