package zzz404.safesql;

import zzz404.safesql.util.CommonUtils;

public class Field {
    private Entity<?> entity;
    private String propertyName;
    String realColumnName;
    private String function;

    public Field(Entity<?> entity, String propertyName) {
        this.entity = entity;
        this.propertyName = propertyName;
        this.realColumnName = propertyName;
        entity.addField(this);
    }

    public Entity<?> getEntity() {
        return entity;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPrefixedRealColumnName() {
        String result = realColumnName;
        if (entity.getIndex() > 0) {
            result = "t" + entity.getIndex() + "." + result;
        }
        if (function != null) {
            result = function + "(" + result + ")";
        }
        return result;
    }

    public void as(Object o) {
        QueryContext ctx = QueryContext.get();
        Field field = ctx.takeLastField();
        ctx.addColumnMapping(propertyName, field.getPropertyName());
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

    public void setRealColumnName(String realColumnName) {
        this.realColumnName = realColumnName;
    }

    public static Field count() {
        Field field = new Field(null, "*");
        field.function = "COUNT";
        return field;
    }

    public static Field count(Field field) {
        field.function = "COUNT";
        return field;
    }

    public static Field all(Object mockedObject) {
        Entity<?> entity = ((EntityGettable) mockedObject).getEntity();
        Field field = new Field(entity, "*");
        return field;
    }

}