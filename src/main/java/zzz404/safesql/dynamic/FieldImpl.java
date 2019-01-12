package zzz404.safesql.dynamic;

import zzz404.safesql.Field;
import zzz404.safesql.sql.TableSchema;
import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public class FieldImpl implements Field {
    private Entity<?> entity;
    private Class<?> clazz;
    private String propertyName;
    private String columnName;
    private String function;

    private FieldImpl asField;

    public FieldImpl(Entity<?> entity, String propertyName) {
        this.entity = entity;
        this.propertyName = propertyName;
        this.columnName = propertyName;
        if (entity != null) {
            entity.addField(this);
        }
    }

    public FieldImpl(Entity<?> entity, String propertyName, Class<?> clazz) {
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

    public void as(Object o) {
        QueryContext ctx = QueryContext.get();
        asField = (FieldImpl) ctx.takeLastField();
        if (asField.entity != ctx.resultEntity) {
            throw new IllegalStateException("fled(...).as(..) can only be used for from(..).to(..) ...");
        }
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

    public static FieldImpl count() {
        FieldImpl field = new FieldImpl(null, "*", Integer.class);
        field.function = "COUNT";
        return field;
    }

    public static FieldImpl all(EntityGettable mockedObject) {
        Entity<?> entity = mockedObject.entity();
        FieldImpl field = new FieldImpl(entity, "*");
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