package zzz404.safesql;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import zzz404.safesql.reflection.ClassAnalyzer;
import zzz404.safesql.sql.QuietResultSet;
import zzz404.safesql.type.ValueType;
import zzz404.safesql.util.Tuple2;

public class TwoTableQuerier<T, U> extends DynamicQuerier {

    private Class<T> class1;
    private Class<U> class2;
    private T mockedObject1;
    private U mockedObject2;

    public TwoTableQuerier(String name, Class<T> class1, Class<U> class2) {
        super(name);
        this.class1 = class1;
        this.class2 = class2;
        this.mockedObject1 = createMockedObject(class1, 1);
        this.mockedObject2 = createMockedObject(class2, 2);

        this.tableColumns = Arrays.asList(new TableColumn(0, "*"));
    }

    public TwoTableQuerier<T, U> select(BiConsumer<T, U> columnsCollector) {
        onSelectScope(() -> {
            columnsCollector.accept(mockedObject1, mockedObject2);
        });
        return this;
    }

    public TwoTableQuerier<T, U> where(BiConsumer<T, U> columnsCollector) {
        onWhereScope(() -> {
            columnsCollector.accept(mockedObject1, mockedObject2);
        });
        return this;
    }

    public TwoTableQuerier<T, U> groupBy(BiConsumer<T, U> columnsCollector) {
        onGroupByScope(() -> {
            columnsCollector.accept(mockedObject1, mockedObject2);
        });
        return this;
    }

    public TwoTableQuerier<T, U> orderBy(BiConsumer<T, U> columnsCollector) {
        onOrderByScope(() -> {
            columnsCollector.accept(mockedObject1, mockedObject2);
        });
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
            
            tables.add(getTableName(class1) + " t1");
        }
        if (allUsedTableIndexes.contains(2)) {
            tables.add(ClassAnalyzer.get(class2).getTableName() + " t2");
        }
        return StringUtils.join(tables, ", ");
    }

    private String getTableName(Class<T> clazz) {
        String tableName = ClassAnalyzer.get(clazz).getTableName();
        return connFactory.getRealTableName(tableName);
    }

    private Tuple2<T, U> rsToTuple(QuietResultSet rs) {
        TableColumnSeparater separater = new TableColumnSeparater(this.tableColumns);
        Set<String> columns1 = separater.getColumnsOfTable(1);
        Set<String> columns2 = separater.getColumnsOfTable(2);

        T t = ValueType.mapRsRowToObject(rs, class1, columns1.toArray(new String[columns1.size()]));
        U u = ValueType.mapRsRowToObject(rs, class2, columns2.toArray(new String[columns1.size()]));
        return new Tuple2<>(t, u);
    }

    static class TableColumnSeparater {
        private List<TableColumn> tableColumns;
        private Set<Integer> tableIds_that_selectAll;
        private Map<Integer, List<TableColumn>> table_columns_map;

        public TableColumnSeparater(List<TableColumn> tableColumns) {
            this.tableColumns = tableColumns;
            Map<Boolean, List<TableColumn>> map = this.tableColumns.stream()
                    .collect(groupingBy(c -> c.getColumnName().equals("*")));
            this.tableIds_that_selectAll = map.get(true).stream().map(TableColumn::getTableIndex).collect(toSet());
            this.table_columns_map = map.get(false).stream().collect(groupingBy(TableColumn::getTableIndex));
        }

        /**
         * return columnNames of the table. 
         * if return null -> means all columns.
         * if return empty set -> means no column.
         */
        public Set<String> getColumnsOfTable(int tableIndex) {
            if (tableIds_that_selectAll.contains(tableIndex)) {
                return null;
            }
            else {
                List<TableColumn> columns = table_columns_map.get(tableIndex);
                if (columns == null) {
                    return Collections.emptySet();
                }
                else {
                    return columns.stream().map(TableColumn::getColumnName).collect(toSet());
                }
            }
        }
    }
}
