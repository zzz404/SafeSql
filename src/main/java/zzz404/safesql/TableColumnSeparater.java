package zzz404.safesql;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

class TableColumnSeparater {
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