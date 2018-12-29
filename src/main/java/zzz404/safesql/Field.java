package zzz404.safesql;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.util.CommonUtils;

public class Field<T> {
    private Entity<?> entity;
    private String propertyName;
    public String realColumnName;
    private String function;
    private Class<T> clazz;
    
    Field<T> toField;

    public Field(Entity<?> entity, String propertyName) {
        this.entity = entity;
        this.propertyName = propertyName;
        this.realColumnName = propertyName;
        if (entity != null) {
            entity.addField(this);
        }
    }

    public Field(Entity<?> entity, String propertyName, Class<T> clazz) {
        this(entity, propertyName);
        this.clazz = clazz;
    }

    public Entity<?> getEntity() {
        return entity;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPrefixedRealColumnName() {
        String result = realColumnName;
        if (entity != null && entity.getIndex() > 0) {
            result = "t" + entity.getIndex() + "." + result;
        }
        if (function != null) {
            result = function + "(" + result + ")";
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void as(T o) {
        QueryContext ctx = QueryContext.get();
        Field<?> field = ctx.takeLastField();
        Validate.isTrue(this.clazz == field.clazz);
        toField = (Field<T>) field;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that,
                tc -> new Object[] { tc.entity, tc.propertyName, tc.realColumnName, tc.function });
    }

    @Override
    public String toString() {
        return getPrefixedRealColumnName();
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

}