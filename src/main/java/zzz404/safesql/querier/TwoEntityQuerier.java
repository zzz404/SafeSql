package zzz404.safesql.querier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import zzz404.safesql.Page;
import zzz404.safesql.TableColumn;
import zzz404.safesql.reflection.ClassAnalyzer;
import zzz404.safesql.reflection.TwoObjectPlayer;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.type.ValueType;
import zzz404.safesql.util.Tuple2;

public class TwoEntityQuerier<T, U> extends DynamicQuerier {

    private Class<T> class1;
    private Class<U> class2;
    T mockedObject1;
    U mockedObject2;

    public TwoEntityQuerier(String name, Class<T> class1, Class<U> class2) {
        super(name);
        this.class1 = class1;
        this.class2 = class2;
        this.mockedObject1 = createMockedObject(class1, 1);
        this.mockedObject2 = createMockedObject(class2, 2);

        this.tableColumns = Arrays.asList(new TableColumn(0, "*"));
    }

    public TwoEntityQuerier<T, U> select(TwoObjectPlayer<T, U> columnsCollector) {
        onSelectScope(() -> {
            columnsCollector.play(mockedObject1, mockedObject2);
        });
        return this;
    }

    public TwoEntityQuerier<T, U> where(TwoObjectPlayer<T, U> columnsCollector) {
        onWhereScope(() -> {
            columnsCollector.play(mockedObject1, mockedObject2);
        });
        return this;
    }

    public TwoEntityQuerier<T, U> groupBy(TwoObjectPlayer<T, U> columnsCollector) {
        onGroupByScope(() -> {
            columnsCollector.play(mockedObject1, mockedObject2);
        });
        return this;
    }

    public TwoEntityQuerier<T, U> orderBy(TwoObjectPlayer<T, U> columnsCollector) {
        onOrderByScope(() -> {
            columnsCollector.play(mockedObject1, mockedObject2);
        });
        return this;
    }

    @Override
    public TwoEntityQuerier<T, U> offset(int offset) {
        super.offset(offset);
        return this;
    }

    @Override
    public TwoEntityQuerier<T, U> limit(int limit) {
        super.limit(limit);
        return this;
    }

    @Override
    public Optional<Tuple2<T, U>> queryOne() {
        return queryOne(rs -> rsToTuple(rs));
    }

    @Override
    public List<Tuple2<T, U>> queryList() {
        return queryList(rs -> rsToTuple(rs));
    }

    @Override
    public Page<Tuple2<T, U>> queryPage() {
        return queryPage(rs -> rsToTuple(rs));
    }

    public <E> E queryEntitiesStream(Function<Stream<Tuple2<T, U>>, E> tupleStreamReader) {
        return queryStream(rsStream -> {
            Stream<Tuple2<T, U>> tupleStream = rsStream.map(rs -> rsToTuple(rs));
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
        return StringUtils.join(tables, ", ");
    }

    private Tuple2<T, U> rsToTuple(QuietResultSet rs) {
        TableColumnSeparater separater = new TableColumnSeparater(this.tableColumns);
        Set<String> columns1 = separater.getColumnsOfTable(1);
        Set<String> columns2 = separater.getColumnsOfTable(2);

        T t = ValueType.mapRsRowToObject(rs, class1, columns1.toArray(new String[columns1.size()]));
        U u = ValueType.mapRsRowToObject(rs, class2, columns2.toArray(new String[columns2.size()]));
        return new Tuple2<>(t, u);
    }
}
