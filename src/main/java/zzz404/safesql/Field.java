package zzz404.safesql;

import zzz404.safesql.util.CommonUtils;

public class Field {
    private Entity<?> entity;
    private String propertyName;
    public String realColumnName;
    private String function;

    public Field(Entity<?> entity, String propertyName) {
        this.entity = entity;
        this.propertyName = propertyName;
        this.realColumnName = propertyName;
        if (entity != null) {
            entity.addField(this);
        }
    }

    public Field(Entity<?> entity, String propertyName, String realColumnName) {
        this.entity = entity;
        this.propertyName = propertyName;
        this.realColumnName = realColumnName;
        if (entity != null) {
            entity.addField(this);
        }
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

    public static Field count() {
        Field field = new Field(null, "*");
        field.function = "COUNT";
        return field;
    }

    public static Field all(EntityGettable mockedObject) {
        Entity<?> entity = mockedObject.entity();
        Field field = new Field(entity, "*");
        return field;
    }

}