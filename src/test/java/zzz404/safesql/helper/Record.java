package zzz404.safesql.helper;

import java.util.ArrayList;

public class Record {
    private ArrayList<String> tables = new ArrayList<>();
    private ArrayList<String> columns = new ArrayList<>();
    private ArrayList<Object> values = new ArrayList<>();

    public static Record[] singleColumn(String columnLabel, int... values) {
        Record[] records = new Record[values.length];
        for (int i = 0; i < values.length; i++) {
            records[i] = new Record().setValue(columnLabel, values[i]);
        }
        return records;
    }

    public static Record[] singleColumn(String columnLabel, String... values) {
        Record[] records = new Record[values.length];
        for (int i = 0; i < values.length; i++) {
            records[i] = new Record().setValue(columnLabel, values[i]);
        }
        return records;
    }

    public Record setValue(String columnName, Object value) {
        this.tables.add(null);
        this.columns.add(columnName);
        this.values.add(value);
        return this;
    }

    public Record setValue(String tableName, String columnName, Object value) {
        this.tables.add(tableName);
        this.columns.add(columnName);
        this.values.add(value);
        return this;
    }

    private int objectToInt(Object o) {
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        }
        else {
            return Integer.parseInt((String) o);
        }
    }

    public int getColumnCount() {
        return columns.size();
    }

    public String getTableName(int index) {
        return tables.get(index - 1);
    }

    public String getColumnName(int index) {
        return columns.get(index - 1);
    }

    public String getString(int index) {
        return get(index).toString();
    }

    public int getInt(int index) {
        return objectToInt(get(index));
    }

    private Object getValue(String columnName) {
        int index = columns.indexOf(columnName);
        return values.get(index);
    }

    public String getString(String columnName) {
        return getValue(columnName).toString();
    }

    public int getInt(String columnName) {
        return objectToInt(getValue(columnName));
    }

    private Object get(int index) {
        return values.get(index - 1);
    }

}
