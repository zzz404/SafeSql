package zzz404.safesql;

public class Sql {

    public static final String BETWEEN = "between";
    public static final String IN = "in";
    public static final String LIKE = "like";

    public static <T> SqlQuerier1<T> from(Class<T> class1) {
        QueryContext ctx = new QueryContext();
        QueryContext.instance.set(ctx);

        return new SqlQuerier1<>(class1);
    }

    @SafeVarargs
    public static <T> Condition cond(T field, String operator, T... values) {
        QueryContext ctx = QueryContext.instance.get();
        Condition cond = Condition.of(operator, values);
        ctx.addCondition(cond);
        return cond;
    }

    public static void asc(Object o) {
        QueryContext ctx = QueryContext.instance.get();
        ctx.addOrderBy(new OrderBy(true));
    }

    public static void desc(Object o) {
        QueryContext ctx = QueryContext.instance.get();
        ctx.addOrderBy(new OrderBy(false));
    }
}
