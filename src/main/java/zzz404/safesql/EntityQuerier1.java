package zzz404.safesql;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import zzz404.safesql.util.CommonUtils;

public class EntityQuerier1<T> extends DynamicQuerier {

    private Class<T> clazz;
    private T mockedObject;

    public EntityQuerier1(Class<T> clazz) {
        this.clazz = clazz;
        this.mockedObject = createMockedObject(clazz, 0);
    }

    public EntityQuerier1<T> select(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.tableColumns = new ArrayList<>(new LinkedHashSet<>(QueryContext.get().takeAllTableColumns()));
        return this;
    }

    public EntityQuerier1<T> where(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.conditions = QueryContext.get().conditions;
        return this;
    }

    public EntityQuerier1<T> orderBy(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.orderBys = QueryContext.get().orderBys;
        return this;
    }

    @Override
    public EntityQuerier1<T> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public EntityQuerier1<T> limit(int limit) {
        super.limit(limit);
        return this;
    }

    public String buildSql() {
        String tableName = ClassAnalyzer.get(clazz).getTableName();
        String sql = "SELECT " + getColumnsClause() + " FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + this.conditions.stream().map(Condition::toClause).collect(Collectors.joining(" AND "));
        }
        if (!this.orderBys.isEmpty()) {
            sql += " ORDER BY " + this.orderBys.stream().map(OrderBy::toClause).collect(Collectors.joining(", "));
        }
        return sql;
    }

    String getColumnsClause() {
        return CommonUtils.join(tableColumns, ", ", TableColumn::getColumnName);
    }

    public String buildSql_for_queryCount() {
        String tableName = ClassAnalyzer.get(clazz).getTableName();
        String sql = "SELECT COUNT(*) FROM " + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + this.conditions.stream().map(Condition::toClause).collect(Collectors.joining(" AND "));
        }
        return sql;
    }

    @Override
    public Optional<T> queryOne() {
        return queryOne(clazz);
    }

    @Override
    public List<T> queryList() {
        return queryList(clazz);
    }

    @Override
    public Page<T> queryPage() {
        return queryPage(clazz);
    }

    public <E> E queryEntityStream(Function<Stream<T>, E> streamReader) {
        return queryStream(clazz, streamReader);
    }

}
