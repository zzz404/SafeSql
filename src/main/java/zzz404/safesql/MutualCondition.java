package zzz404.safesql;

import java.util.List;

import zzz404.safesql.sql.type.TypedValue;
import zzz404.safesql.util.CommonUtils;

public class MutualCondition<T> extends AbstractCondition {

    private String operator;
    protected Field<T> field2;

    public MutualCondition(Field<T> tableColumn, String operator, Field<T> tableColumn2) {
        super(tableColumn);
        this.operator = operator;
        this.field2 = tableColumn2;
    }

    @Override
    public boolean equals(Object that) {
        return CommonUtils.isEquals(this, that, o -> new Object[] { o.field, o.operator, o.field2 });
    }

    @Override
    public String toString() {
        return toClause();
    }

    @Override
    public String toClause() {
        return field.getPrefixedColumnName() + " " + operator + " " + field2.getPrefixedColumnName();
    }

    @Override
    public void appendValuesTo(List<TypedValue<?>> paramValues) {
    }

}
