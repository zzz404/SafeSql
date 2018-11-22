package zzz404.safesql;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.util.OneObjectPlayer;

public class OneEntityQuerier<T> extends DynamicQuerier {

    private Class<T> clazz;
    private T mockedObject;

    public OneEntityQuerier(String name, Class<T> clazz) {
        super(name);
        this.clazz = clazz;
        this.mockedObject = createMockedObject(clazz, 0);
    }

    public OneEntityQuerier<T> select(OneObjectPlayer<T> columnsCollector) {
        onSelectScope(() -> {
            columnsCollector.play(mockedObject);
        });
        return this;
    }

    public OneEntityQuerier<T> where(OneObjectPlayer<T> conditionsCollector) {
        onWhereScope(() -> {
            conditionsCollector.play(mockedObject);
        });
        return this;
    }

    public OneEntityQuerier<T> groupBy(OneObjectPlayer<T> columnsCollector) {
        onGroupByScope(() -> {
            columnsCollector.play(mockedObject);
        });
        return this;
    }

    public OneEntityQuerier<T> orderBy(OneObjectPlayer<T> columnsCollector) {
        onOrderByScope(() -> {
            columnsCollector.play(mockedObject);
        });
        return this;
    }

    @Override
    public OneEntityQuerier<T> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public OneEntityQuerier<T> limit(int limit) {
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
        return getRealTableName(clazz);
    }

}
