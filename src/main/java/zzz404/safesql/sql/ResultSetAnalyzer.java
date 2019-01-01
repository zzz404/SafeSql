package zzz404.safesql.sql;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.map.MultiValueMap;

import zzz404.safesql.sql.proxy.QuietResultSet;
import zzz404.safesql.sql.proxy.QuietResultSetMetaData;

public class ResultSetAnalyzer {
    private QuietResultSet rs;
    private QuietResultSetMetaData meta;

    private List<ColumnInfo> columnInfos = null;
    private MultiValueMap<String, ColumnInfo> tableColumnsMap = null;

    public ResultSetAnalyzer(ResultSet rs) {
        this.rs = new QuietResultSet(rs);
        this.meta = new QuietResultSetMetaData(this.rs.getMetaData());
    }

    public String getLcColumnOfAutoIncrement() {
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            if (meta.isAutoIncrement(i)) {
                return meta.getColumnLabel(i).toLowerCase();
            }
        }
        return null;
    }

    public Collection<ColumnInfo> getAllColumns() {
        if (columnInfos == null) {
            columnInfos = IntStream.range(1, meta.getColumnCount() + 1)
                    .mapToObj(i -> new ColumnInfo(i, meta.getColumnLabel(i))).collect(Collectors.toList());
        }
        return columnInfos;
    }

    public Collection<ColumnInfo> getAllColumns(String lcTableName) {
        if (tableColumnsMap == null) {
            tableColumnsMap = new MultiValueMap<>();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                tableColumnsMap.put(meta.getTableName(i).toLowerCase(), new ColumnInfo(i, meta.getColumnLabel(i)));
            }
        }
        return tableColumnsMap.getCollection(lcTableName);
    }

    public static class ColumnInfo {
        public int index;
        public String column;
        public String lcColumn;

        public ColumnInfo(int index, String column) {
            this.index = index;
            this.column = column;
            this.lcColumn = column.toLowerCase();
        }
    }
}
