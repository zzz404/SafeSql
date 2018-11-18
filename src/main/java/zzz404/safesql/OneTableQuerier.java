package zzz404.safesql;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class OneTableQuerier<T> extends DynamicQuerier {

    private Class<T> clazz;
    private T mockedObject;

    public OneTableQuerier(Class<T> clazz) {
        this.clazz = clazz;
        this.mockedObject = createMockedObject(clazz, 0);
    }

    public OneTableQuerier<T> select(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.tableColumns = QueryContext.get().takeAllTableColumns();
        return this;
    }

    public OneTableQuerier<T> where(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.conditions = QueryContext.get().conditions;
        return this;
    }

    public OneTableQuerier<T> orderBy(Consumer<T> consumer) {
        consumer.accept(mockedObject);
        this.orderBys = QueryContext.get().orderBys;
        return this;
    }

    @Override
    public OneTableQuerier<T> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public OneTableQuerier<T> limit(int limit) {
        super.limit(limit);
        return this;
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

    @Override
    protected String getTablesClause() {
        return ClassAnalyzer.get(clazz).getTableName();
    }

}
