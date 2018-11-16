package zzz404.safesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import net.sf.cglib.proxy.Enhancer;

public class SqlQuerier1<T> extends SqlQuerier {

    private Class<T> clazz;
    private T mockedObject;

    public SqlQuerier1(Class<T> clazz) {
        this.clazz = clazz;
        this.mockedObject = createMockedObject();
        
        this.columnNames = Arrays.asList("*");
    }

    private T createMockedObject() {
        Enhancer en = new Enhancer();
        en.setSuperclass(clazz);
        GetterTracer<T> getterLogger = new GetterTracer<>(clazz);
        en.setCallback(getterLogger);

        @SuppressWarnings("unchecked")
        T mockedObject = (T) en.create();
        return mockedObject;
    }

    public SqlQuerier1<T> select(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.columnNames = new ArrayList<>(new LinkedHashSet<>(
                QueryContext.INSTANCE.get().takeAllColumnNames()));
        return this;
    }

    public SqlQuerier1<T> where(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.conditions = QueryContext.INSTANCE.get().conditions;
        return this;
    }

    public SqlQuerier1<T> orderBy(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.orderBys = QueryContext.INSTANCE.get().orderBys;
        return this;
    }

    @Override
    public SqlQuerier1<T> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public SqlQuerier1<T> limit(int limit) {
        super.limit(limit);
        return this;
    }

    public String buildSql() {
        String tableName = ClassAnalyzer.get(clazz).getTableName();
        String sql = "SELECT " + StringUtils.join(columnNames, ", ") + " FROM "
                + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + this.conditions.stream().map(Condition::toClause)
                    .collect(Collectors.joining(" AND "));
        }
        if (!this.orderBys.isEmpty()) {
            sql += " ORDER BY " + this.orderBys.stream().map(OrderBy::toClause)
                    .collect(Collectors.joining(", "));
        }
        return sql;
    }

    public String buildSql_for_queryCount() {
        String tableName = ClassAnalyzer.get(clazz).getTableName();
        String sql = "SELECT COUNT(*) FROM "
                + tableName;
        if (!this.conditions.isEmpty()) {
            sql += " WHERE " + this.conditions.stream().map(Condition::toClause)
                    .collect(Collectors.joining(" AND "));
        }
        return sql;
    }

    @Override
    public Optional<T> queryOne() {
        return queryOne(clazz);
    }

    @Override
    public Page<T> queryPage() {
        return queryPage(clazz);
    }
}
