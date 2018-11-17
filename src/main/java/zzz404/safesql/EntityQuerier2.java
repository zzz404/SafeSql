package zzz404.safesql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class EntityQuerier2<T, U> extends DynamicQuerier {

    private Class<T> class1;
    private Class<U> class2;
    private T mockedObject1;
    private U mockedObject2;

    public EntityQuerier2(Class<T> class1, Class<U> class2) {
        this.class1 = class1;
        this.class2 = class2;
        this.mockedObject1 = createMockedObject(class1);
        this.mockedObject2 = createMockedObject(class2);

        this.columnNames = Arrays.asList("*");
    }

    public EntityQuerier2<T, U> select(BiConsumer<T, U> consumer) {
        consumer.accept(mockedObject1, mockedObject2);
        this.columnNames = new ArrayList<>(new LinkedHashSet<>(
                QueryContext.INSTANCE.get().takeAllColumnNames()));
        return this;
    }

    public EntityQuerier2<T, U> where(BiConsumer<T, U> consumer) {
        consumer.accept(mockedObject1, mockedObject2);
        this.conditions = QueryContext.INSTANCE.get().conditions;
        return this;
    }

    public EntityQuerier2<T, U> orderBy(BiConsumer<T, U> consumer) {
        consumer.accept(mockedObject1, mockedObject2);
        this.orderBys = QueryContext.INSTANCE.get().orderBys;
        return this;
    }

    @Override
    public EntityQuerier2<T, U> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public EntityQuerier2<T, U> limit(int limit) {
        super.limit(limit);
        return this;
    }

    public String buildSql() {
        // String tableName = ClassAnalyzer.get(class1, class2).getTableName();
        // String sql = "SELECT " + StringUtils.join(columnNames, ", ") + " FROM
        // "
        // + tableName;
        // if (!this.conditions.isEmpty()) {
        // sql += " WHERE " + this.conditions.stream().map(Condition::toClause)
        // .collect(Collectors.joining(" AND "));
        // }
        // if (!this.orderBys.isEmpty()) {
        // sql += " ORDER BY " + this.orderBys.stream().map(OrderBy::toClause)
        // .collect(Collectors.joining(", "));
        // }
        // return sql;
        // TODO
        return null;
    }

    public String buildSql_for_queryCount() {
        // String tableName = ClassAnalyzer.get(class1, class2).getTableName();
        // String sql = "SELECT COUNT(*) FROM "
        // + tableName;
        // if (!this.conditions.isEmpty()) {
        // sql += " WHERE " + this.conditions.stream().map(Condition::toClause)
        // .collect(Collectors.joining(" AND "));
        // }
        // return sql;
        // TODO
        return null;
    }

    @Override
    public Optional<Tuple2<T, U>> queryOne() {
        // return queryOne(class1, class2);
        // TODO
        return null;
    }

    @Override
    public Page<Tuple2<T, U>> queryPage() {
        // return queryPage(class1, class2);
        // TODO
        return null;
    }

    @Override
    public List<?> queryList() {
        // TODO Auto-generated method stub
        return null;
    }

    public <E> E queryEntitiesStream(Function<Stream<Tuple2<T, U>>, E> streamReader) {
        // TODO
        return null;
    }
}
