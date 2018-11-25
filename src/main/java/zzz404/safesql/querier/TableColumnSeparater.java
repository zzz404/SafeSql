package zzz404.safesql.querier;

import static java.util.stream.Collectors.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import zzz404.safesql.TableField;

class TableColumnSeparater {
    private List<TableField> tableColumns;
    private Set<Integer> tableIds_that_selectAll;
    private Map<Integer, List<TableField>> table_columns_map;

    public TableColumnSeparater(List<TableField> tableColumns) {
        this.tableColumns = tableColumns;
        Map<Boolean, List<TableField>> map = this.tableColumns.stream()
                .collect(groupingBy(c -> c.getPropertyName().equals("*")));
        this.tableIds_that_selectAll = map.get(true).stream().map(TableField::getEntityIndex).collect(toSet());
        this.table_columns_map = map.get(false).stream().collect(groupingBy(TableField::getEntityIndex));
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
            List<TableField> columns = table_columns_map.get(tableIndex);
            if (columns == null) {
                return Collections.emptySet();
            }
            else {
                return columns.stream().map(TableField::getPropertyName).collect(toSet());
            }
        }
    }
}