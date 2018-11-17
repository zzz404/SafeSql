package zzz404.safesql.helper;

public class Record {
    private Object[] values;

    public static Record singleColumn(int value) {
        return new Record().setValues(value);
    }

    public static Record singleColumn(String value) {
        return new Record().setValues(value);
    }

    public Record setValues(Integer... values) {
        this.values = values;
        return this;
    }

    public Record setValues(String... values) {
        this.values = values;
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

    private Object get(int index) {
        return values[index - 1];
    }

}
