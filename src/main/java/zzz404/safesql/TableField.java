package zzz404.safesql;

import zzz404.safesql.util.CommonUtils;

public final class TableField {
    private Entity<?> entity;
    private String propertyName;
    private String realColumnName;

    public TableField(Entity<?> entity, String propertyName) {
        this.entity = entity;
        this.propertyName = propertyName;
        this.realColumnName = propertyName;
    }

    public Entity<?> getEntity() {
        return entity;
    }

    public int getEntityIndex() {
        return entity.getIndex();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPrefixedColumnName() {
        if (entity.getIndex() > 0) {
            return "t" + entity.getIndex() + "." + realColumnName;
        }
        else {
            return realColumnName;
        }
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, tc -> new Object[] { tc.entity, tc.propertyName, realColumnName });
    }

    @Override
    public String toString() {
        return getPrefixedColumnName();
    }

    public void setRealColumnName(String realColumnName) {
        this.realColumnName = realColumnName;
    }

}