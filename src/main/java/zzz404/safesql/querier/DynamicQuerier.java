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
import zzz404.safesql.TableField;
import zzz404.safesql.sql.DbSourceImpl;
import zzz404.safesql.util.CommonUtils;

public abstract class DynamicQuerier extends SqlQuerier {

    protected List<Entity<?>> entities = new ArrayList<>();
    protected List<TableField> tableFields = Collections.emptyList();
    protected List<AbstractCondition> conditions = Collections.emptyList();
    protected List<TableField> groupBys = Collections.emptyList();
    protected List<OrderBy> orderBys = Collections.emptyList();

    private Scope currentScope = null;
    private transient Map<Entity<?>, List<TableField>> entity_fields_map = null;

    public DynamicQuerier(DbSourceImpl connFactory) {
        super(connFactory);
    }

    protected void onSelectScope(Runnable collectColumns) {
        checkScope(Scope.select);
        QueryContext.underQueryContext(ctx -> {
            ctx.setScope(Scope.select);

            collectColumns.run();

            this.tableFields = ctx.takeAllTableColumnsUniquely();
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

            this.groupBys = ctx.takeAllTableColumnsUniquely();
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
        List<Entity<?>> usedEntities = tableFields.isEmpty() ? entities
                : entities.stream().filter(entity -> !entity.getFields().isEmpty()).collect(Collectors.toList());
        return usedEntities.stream()
                .map(entity -> dbSource.getRealTableName(entity.getVirtualTableName()) + " t" + entity.getIndex())
                .collect(Collectors.joining(", "));
    }

    public String sql() {
        dbSource.revise(entities);
        String tableName = getTablesClause();
        String sql = "SELECT " + getColumnsClause() + " FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + getConditionsClause();
        }
        if (!this.orderBys.isEmpty()) {
            sql += " ORDER BY " + this.orderBys.stream().map(OrderBy::toClause).collect(Collectors.joining(", "));
        }
        return sql;
    }

    String getConditionsClause() {
        return this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
    }

    String getColumnsClause() {
        if (tableFields.isEmpty()) {
            return "*";
        }
        return CommonUtils.join(tableFields, ", ", TableField::getPrefixedColumnName);
    }

    public String sql_for_queryCount() {
        String tableName = getTablesClause();
        String sql = "SELECT COUNT(*) FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE "
                    + this.conditions.stream().map(AbstractCondition::toClause).collect(Collectors.joining(" AND "));
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

    protected List<TableField> getTableFieldsOfEntity(Entity<?> entity) {
        if (entity_fields_map == null) {
            entity_fields_map = tableFields.stream().collect(Collectors.groupingBy(TableField::getEntity));
        }
        return entity_fields_map.get(entity);
    }

    public abstract Object queryOne();

    public abstract List<?> queryList();

    public abstract Page<?> queryPage();

}
