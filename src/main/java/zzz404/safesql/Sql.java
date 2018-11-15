package zzz404.safesql;

public class Sql {

    public static final String BETWEEN = "between";
    public static final String IN = "in";
    public static final String LIKE = "like";
    
    private Sql() {}

    public static <T> SqlQuerier1<T> from(Class<T> class1) {
        QueryContext ctx = new QueryContext();
        QueryContext.INSTANCE.set(ctx);

        return new SqlQuerier1<>(class1);
    }

    @SafeVarargs
    public static <T> Condition cond(T field, String operator, T... values) {
        QueryContext ctx = QueryContext.INSTANCE.get();
        Condition cond = Condition.of(ctx.takeColumnName(), operator, values);
        ctx.conditions.add(cond);
        return cond;
    }

    public static void asc(Object o) {
        addOrderByToContext(true);
    }

    private static void addOrderByToContext(boolean isAsc) {
        QueryContext ctx = QueryContext.INSTANCE.get();
        ctx.orderBys.add(new OrderBy(ctx.takeColumnName(), isAsc));
    }
    
    public static void desc(Object o) {
        addOrderByToContext(false);
    }
}
