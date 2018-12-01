package zzz404.safesql.querier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

import zzz404.safesql.AbstractCondition;
import zzz404.safesql.Entity;
import zzz404.safesql.OrderBy;
import zzz404.safesql.Page;
import zzz404.safesql.QueryContext;
import zzz404.safesql.Scope;
import zzz404.safesql.Field;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.util.CommonUtils;

public abstract class DynamicQuerier extends SqlQuerier {

    protected List<Entity<?>> entities = new ArrayList<>();
    protected List<Field> fields = Collections.emptyList();
    protected List<AbstractCondition> conditions = Collections.emptyList();
    protected List<Field> groupBys = Collections.emptyList();
    protected List<OrderBy> orderBys = Collections.emptyList();

    private Scope currentScope = null;
    private transient Map<Entity<?>, List<Field>> entity_fields_map = null;

    public DynamicQuerier(DbSourceImpl connFactory) {
        super(connFactory);
    }

    protected void onSelectScope(Runnable collectColumns) {
        checkScope(Scope.select);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.select);

            collectColumns.run();

            this.fields = ctx.takeAllTableFieldsUniquely();
        });
    }

    private void checkScope(Scope scope) {
        Scope previousScope = this.currentScope;
        Validate.isTrue(previousScope == null || scope.ordinal() >= previousScope.ordinal());
        currentScope = scope;
    }

    protected void onWhereScope(Runnable collectConditions) {
        checkScope(Scope.where);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.where);

            collectConditions.run();

            this.conditions = ctx.getConditions();
        });
    }

    protected void onGroupByScope(Runnable collectColumns) {
        checkScope(Scope.groupBy);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.groupBy);

            collectColumns.run();

            this.groupBys = ctx.takeAllTableFieldsUniquely();
        });
    }

    protected void onOrderByScope(Runnable collectColumns) {
        checkScope(Scope.orderBy);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.orderBy);

            collectColumns.run();

            this.orderBys = ctx.getOrderBys();
        });
    }

    String getTablesClause() {
        List<Entity<?>> usedEntities = fields.isEmpty() ? entities
                : entities.stream().filter(entity -> !entity.getFields().isEmpty()).collect(Collectors.toList());
        return usedEntities.stream()
                .map(entity -> dbSource.getRealTableName(entity.getVirtualTableName()) + " t" + entity.getIndex())
                .collect(Collectors.joining(", "));
    }

    void reviseOrderBys() {
        for (OrderBy orderBy : orderBys) {
            if (orderBy.getTableField() == null) {
                String columnName = orderBy.getColumnName();
                Entity<?> entity = dbSource.chooseEntity(entities, columnName);
                if (entity != null) {
                    orderBy.setEntity(entity);
                    dbSource.revise(orderBy.getTableField());
                }
            }
        }
    }

    String getColumnsClause() {
        if (fields.isEmpty()) {
            return "*";
        }
        return CommonUtils.join(fields, ", ", Field::getPrefixedPropertyName);
    }

    String getConditionsClause() {
        return this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
    }

    String getGroupByClause() {
        return this.groupBys.stream().map(Field::getPrefixedPropertyName).collect(Collectors.joining(", "));
    }

    String getOrderByClause() {
        return this.orderBys.stream().map(OrderBy::toClause).collect(Collectors.joining(", "));
    }

    public String sql() {
        dbSource.revise(entities);
        reviseOrderBys();
        String tableName = getTablesClause();
        String sql = "SELECT " + getColumnsClause() + " FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + getConditionsClause();
        }
        if (!this.groupBys.isEmpty()) {
            sql += " GROUP BY " + getGroupByClause();
        }
        if (!this.orderBys.isEmpty()) {
            sql += " ORDER BY " + getOrderByClause();
        }
        return sql;
    }

    public String sql_for_queryCount() {
        String sql = "SELECT COUNT(*) FROM " + getTablesClause();
        if (!this.conditions.isEmpty()) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
        }
        if (!this.groupBys.isEmpty()) {
            sql += " GROUP BY " + getGroupByClause();
        }
        return sql;
    }

    @Override
    protected Object[] paramValues() {
        ArrayList<Object> paramValues = new ArrayList<>();
        conditions.forEach(cond -> {
            cond.appendValuesTo(paramValues);
        });
        return paramValues.toArray();
    }

    protected List<Field> getFieldsOfEntity(Entity<?> entity) {
        if (entity_fields_map == null) {
            entity_fields_map = fields.stream().collect(Collectors.groupingBy(Field::getEntity));
        }
        return entity_fields_map.get(entity);
    }

    public abstract Object queryOne();

    public abstract List<?> queryList();

    public abstract Page<?> queryPage();

}
