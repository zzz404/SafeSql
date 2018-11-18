package zzz404.safesql;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import zzz404.safesql.util.Tuple2;

public class TwoTableQuerier<T, U> extends DynamicQuerier {

    private Class<T> class1;
    private Class<U> class2;
    private T mockedObject1;
    private U mockedObject2;

    public TwoTableQuerier(Class<T> class1, Class<U> class2) {
        this.class1 = class1;
        this.class2 = class2;
        this.mockedObject1 = createMockedObject(class1, 1);
        this.mockedObject2 = createMockedObject(class2, 2);

        this.tableColumns = Arrays.asList(new TableColumn(0, "*"));
    }

    public TwoTableQuerier<T, U> select(BiConsumer<T, U> consumer) {
        consumer.accept(mockedObject1, mockedObject2);
        this.tableColumns = QueryContext.get().takeAllTableColumns();
        return this;
    }

    public TwoTableQuerier<T, U> where(BiConsumer<T, U> consumer) {
        consumer.accept(mockedObject1, mockedObject2);
        this.conditions = QueryContext.get().conditions;
        return this;
    }

    public TwoTableQuerier<T, U> orderBy(BiConsumer<T, U> consumer) {
        consumer.accept(mockedObject1, mockedObject2);
        this.orderBys = QueryContext.get().orderBys;
        return this;
    }

    @Override
    public TwoTableQuerier<T, U> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public TwoTableQuerier<T, U> limit(int limit) {
        super.limit(limit);
        return this;
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

    @Override
    protected String getTablesClause() {
        return ClassAnalyzer.get(class1).getTableName() + " t1, " + ClassAnalyzer.get(class2).getTableName() + " t2";
    }
}
