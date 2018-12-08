package zzz404.safesql.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Record {
    private ArrayList<Object> values = new ArrayList<>();
    private Map<String, Integer> col_pos_map = new HashMap<>();

    public static Record[] singleColumn(Object... values) {
        Record[] records = new Record[values.length];
        for (int i = 0; i < values.length; i++) {
            records[i] = new Record().setValues(values[i]);
        }
        return records;
    }

    public Record setValues(Object... values) {
        this.values.addAll(Arrays.asList(values));
        return this;
    }

    public Record setValue(String columnName, Object value) {
        this.col_pos_map.put(columnName, values.size());
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

    public String getString(int index) {
        return get(index).toString();
    }

    public int getInt(int index) {
        return objectToInt(get(index));
    }

    private Object getValue(String columnName) {
        return values.get(col_pos_map.get(columnName));
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
