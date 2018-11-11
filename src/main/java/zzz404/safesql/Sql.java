package zzz404.safesql;

public class Sql {

    public static final String BETWEEN = "between";
    public static final String IN = "in";
    public static final String LIKE = "like";

    public static <T> SqlQuerier1<T> from(Class<T> class1) {
        return new SqlQuerier1<>(class1);
    }

    @SafeVarargs
    public static <T> void cond(T field, String operator, T... values) {
        ConditionBuilder builder = ConditionBuilder.instance.get();
        
        builder.operators.add(operator);
        builder.values.add(values);
    }

    public static void asc(Object o) {
    }

    public static void all(Object o) {

    }
}
