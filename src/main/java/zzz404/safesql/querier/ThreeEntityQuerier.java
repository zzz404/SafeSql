package zzz404.safesql.querier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import zzz404.safesql.ConnectionFactoryImpl;
import zzz404.safesql.Page;
import zzz404.safesql.TableColumn;
import zzz404.safesql.reflection.ClassAnalyzer;
import zzz404.safesql.reflection.ThreeObjectPlayer;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.type.ValueType;
import zzz404.safesql.util.Tuple3;

public class ThreeEntityQuerier<T, U, V> extends DynamicQuerier {

    private Class<T> class1;
    private Class<U> class2;
    private Class<V> class3;
    T mockedObject1;
    U mockedObject2;
    V mockedObject3;

    public ThreeEntityQuerier(ConnectionFactoryImpl connFactory, Class<T> class1, Class<U> class2, Class<V> class3) {
        super(connFactory);
        this.class1 = class1;
        this.class2 = class2;
        this.class3 = class3;
        this.mockedObject1 = createMockedObject(class1, 1);
        this.mockedObject2 = createMockedObject(class2, 2);
        this.mockedObject3 = createMockedObject(class3, 3);

        this.tableColumns = Arrays.asList(new TableColumn(0, "*"));
    }

    public ThreeEntityQuerier<T, U, V> select(ThreeObjectPlayer<T, U, V> columnsCollector) {
        onSelectScope(() -> {
            columnsCollector.play(mockedObject1, mockedObject2, mockedObject3);
        });
        return this;
    }

    public ThreeEntityQuerier<T, U, V> where(ThreeObjectPlayer<T, U, V> columnsCollector) {
        onWhereScope(() -> {
            columnsCollector.play(mockedObject1, mockedObject2, mockedObject3);
        });
        return this;
    }

    public ThreeEntityQuerier<T, U, V> groupBy(ThreeObjectPlayer<T, U, V> columnsCollector) {
        onGroupByScope(() -> {
            columnsCollector.play(mockedObject1, mockedObject2, mockedObject3);
        });
        return this;
    }

    public ThreeEntityQuerier<T, U, V> orderBy(ThreeObjectPlayer<T, U, V> columnsCollector) {
        onOrderByScope(() -> {
            columnsCollector.play(mockedObject1, mockedObject2, mockedObject3);
        });
        return this;
    }

    @Override
    public ThreeEntityQuerier<T, U, V> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public ThreeEntityQuerier<T, U, V> limit(int limit) {
        super.limit(limit);
        return this;
    }

    @Override
    public Optional<Tuple3<T, U, V>> queryOne() {
        return queryOne(rs -> rsToTuple(rs));
    }

    @Override
    public List<Tuple3<T, U, V>> queryList() {
        return queryList(rs -> rsToTuple(rs));
    }

    @Override
    public Page<Tuple3<T, U, V>> queryPage() {
        return queryPage(rs -> rsToTuple(rs));
    }

    public <E> E queryEntitiesStream(Function<Stream<Tuple3<T, U, V>>, E> tupleStreamReader) {
        return queryStream(rsStream -> {
            Stream<Tuple3<T, U, V>> tupleStream = rsStream.map(rs -> rsToTuple(rs));
            return tupleStreamReader.apply(tupleStream);
        });
    }

    @Override
    protected String getTablesClause() {
        Set<Integer> allUsedTableIndexes = getAllUsedTableIndexes();
        ArrayList<String> tables = new ArrayList<>(2);
        if (allUsedTableIndexes.contains(1)) {
            tables.add(getRealTableName(class1) + " t1");
        }
        if (allUsedTableIndexes.contains(2)) {
            tables.add(ClassAnalyzer.get(class2).getTableName() + " t2");
        }
        if (allUsedTableIndexes.contains(3)) {
            tables.add(ClassAnalyzer.get(class2).getTableName() + " t3");
        }
        return StringUtils.join(tables, ", ");
    }

    private Tuple3<T, U, V> rsToTuple(QuietResultSet rs) {
        TableColumnSeparater separater = new TableColumnSeparater(this.tableColumns);
        Set<String> columns1 = separater.getColumnsOfTable(1);
        Set<String> columns2 = separater.getColumnsOfTable(2);
        Set<String> columns3 = separater.getColumnsOfTable(3);

        T t = ValueType.mapRsRowToObject(rs, class1, columns1.toArray(new String[columns1.size()]));
        U u = ValueType.mapRsRowToObject(rs, class2, columns2.toArray(new String[columns2.size()]));
        V v = ValueType.mapRsRowToObject(rs, class3, columns3.toArray(new String[columns3.size()]));
        return new Tuple3<>(t, u, v);
    }

}
